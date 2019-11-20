package com.smartling.api.v2.client;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.smartling.api.v2.authentication.AuthenticationApi;
import com.smartling.api.v2.authentication.AuthenticationApiFactory;
import com.smartling.api.v2.client.auth.AbstractBearerAuthFilter;
import com.smartling.api.v2.client.auth.Authenticator;
import com.smartling.api.v2.client.auth.AuthorizationRequestFilter;
import com.smartling.api.v2.client.auth.BearerAuthSecretFilter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;

import org.jboss.resteasy.spi.ResteasyProviderFactory;

/**
 * Abstract factory to create JAX-RS proxies for Smartling APIs.
 *
 * @param <T> the API interface type to proxy
 */
public abstract class AbstractApiFactory<T> implements ApiFactory<T>
{
    private static final String DEFAULT_API_HOST              = "api.smartling.com";
    protected static final String DEFAULT_API_HOST_AND_PROTOCOL = "https://" + DEFAULT_API_HOST;
    protected static final URL DEFAULT_API_URL;

    static
    {
        try
        {
            DEFAULT_API_URL = new URL(DEFAULT_API_HOST_AND_PROTOCOL);
        }
        catch (MalformedURLException e)
        {
            throw new IllegalStateException(e);
        }
    }

    private final URL baseUrl;
    private final ClientFactory clientFactory;

    protected AbstractApiFactory()
    {
        this(DEFAULT_API_URL, new ClientFactory());
    }

    protected AbstractApiFactory(final URL baseUrl)
    {
       this(baseUrl, new ClientFactory());
    }

    protected AbstractApiFactory(final URL baseUrl, final ClientFactory clientFactory)
    {
        Objects.requireNonNull(baseUrl, "baseUrl required");
        Objects.requireNonNull(clientFactory, "clientFactory required");
        this.baseUrl = baseUrl;
        this.clientFactory = clientFactory;
    }

    /*
    protected AbstractApiFactory(final ClientFactory clientFactory)
    {
        this.baseUrl = DEFAULT_API_URL;
        this.clientFactory = clientFactory;
    }
     */

    protected abstract Class<T> getApiClass();

    protected URL getBaseUrl()
    {
        return baseUrl;
    }

    String getBaseUrlHostAndProtocol()
    {
        URL baseUrl = this.getBaseUrl();
        return String.format(Locale.US, "%s://%s", baseUrl.getProtocol(), baseUrl.getHost());
    }

    protected List<ClientRequestFilter> getClientRequestFilters()
    {
        return Collections.emptyList();
    }

    protected List<ClientResponseFilter> getClientResponseFilters()
    {
        return Collections.emptyList();
    }

    protected Map<Class<?>, JsonDeserializer<?>> getDeserializerMap()
    {
        return clientFactory.getDefaultDeserializerMap();
    }

    protected HttpClientConfiguration getHttpClientConfiguration()
    {
        return new HttpClientConfiguration();
    }

    ResteasyProviderFactory getResteasyProviderFactory()
    {
        return null;
    }

    @Override
    public T buildApi(final String userIdentifier, final String userSecret)
    {
        Objects.requireNonNull(userIdentifier, "userIdentifier must be defined");
        Objects.requireNonNull(userSecret, "userSecret must be defined");

        final AuthenticationApi authenticationApi = new AuthenticationApiFactory().buildApi();
        final Authenticator authenticator = new Authenticator(userIdentifier, userSecret, authenticationApi);
        final BearerAuthSecretFilter bearerAuthSecretFilter = new BearerAuthSecretFilter(authenticator);
        return buildApi(bearerAuthSecretFilter);
    }

    @Override
    public T buildApi(final AuthorizationRequestFilter authFilter)
    {
        List<ClientRequestFilter> requestFilters = new ArrayList<>(this.getClientRequestFilters());
        String hostAndProtocol = this.getBaseUrlHostAndProtocol();
        HttpClientConfiguration httpClientConfiguration = this.getHttpClientConfiguration();
        ResteasyProviderFactory providerFactory = this.getResteasyProviderFactory();

        Objects.requireNonNull(authFilter, "authFilter must be defined");
        Objects.requireNonNull(hostAndProtocol, "hostAndProtocol must be defined");
        Objects.requireNonNull(httpClientConfiguration, "httpClientConfiguration must be defined");

        requestFilters.add(authFilter);

        return clientFactory.build(requestFilters, getClientResponseFilters(), hostAndProtocol, getApiClass(), getDeserializerMap(), httpClientConfiguration, providerFactory);
    }
}
