package com.smartling.api.v2.client;

import com.smartling.api.v2.authentication.AuthenticationApi;
import com.smartling.api.v2.authentication.AuthenticationApiFactory;
import com.smartling.api.v2.client.auth.Authenticator;
import com.smartling.api.v2.client.auth.AuthorizationRequestFilter;
import com.smartling.api.v2.client.auth.BearerAuthSecretFilter;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.ws.rs.client.ClientRequestFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Abstract factory to create JAX-RS proxies for Smartling APIs.
 *
 * @param <T> the API interface type to proxy
 */
@Slf4j
public abstract class AbstractApiFactory<T> implements ApiFactory<T>
{
    static ClientFactory defaultClientFactory()
    {
        try
        {
            Class<?> cls = Class.forName("net.smartling.api.internal.client.InternalClientFactory");
            return (ClientFactory) cls.newInstance();
        }
        catch (ClassNotFoundException e)
        {
            log.debug("Using {}", ClientFactory.class);
        }
        catch (IllegalAccessException | InstantiationException e)
        {
            log.error("Unable to load instantiate internal client factory", e);
        }

        return new ClientFactory();
    }
    private ClientFactory clientFactory;

    /**
     * Constructs a new abstract API factory.
     */
    protected AbstractApiFactory()
    {
        this(defaultClientFactory());
    }

    /**
     * Constructs a new abstract API factory with the given client factory.
     *
     * @param clientFactory the <code>ClientFactory</code> to use to construct JAX-RS proxies (required)
     */
    protected AbstractApiFactory(final ClientFactory clientFactory)
    {
        Objects.requireNonNull(clientFactory, "clientFactory required");
        this.clientFactory = clientFactory;
    }

    /***
     * Returns the class of type <code>T</code>.
     *
     * @return the class of <code>T</code>
     */
    protected abstract Class<T> getApiClass();

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
        return buildApi(authFilter, new DefaultClientConfiguration());
    }

    @Override
    public T buildApi(final AuthorizationRequestFilter authFilter, ClientConfiguration config)
    {
        List<ClientRequestFilter> requestFilters = new ArrayList<>(config.getClientRequestFilters());
        HttpClientConfiguration httpClientConfiguration = config.getHttpClientConfiguration();
        ResteasyProviderFactory providerFactory = config.getResteasyProviderFactory();

        Objects.requireNonNull(authFilter, "authFilter must be defined");
        Objects.requireNonNull(httpClientConfiguration, "httpClientConfiguration must be defined");

        requestFilters.add(authFilter);

        return clientFactory.build(
            requestFilters,
            config.getClientResponseFilters(),
            getProtocolAndHost(config.getBaseUrl()),
            getApiClass(),
            httpClientConfiguration,
            providerFactory);
    }

    private static String getProtocolAndHost(URL baseUrl)
    {
        Objects.requireNonNull(baseUrl, "baseUrl required");
        return baseUrl.toString();
    }
}
