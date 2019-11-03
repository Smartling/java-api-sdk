package com.smartling.api.v2.client;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.smartling.api.v2.client.auth.AbstractBearerAuthFilter;
import com.smartling.api.v2.client.auth.BearerAuthSecretFilter;

import java.util.*;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;

import org.jboss.resteasy.spi.ResteasyProviderFactory;

public abstract class AbstractApiFactory<T>
{
    private static final String DEFAULT_API_HOST              = "api.smartling.com";
    protected static final String DEFAULT_API_HOST_AND_PROTOCOL = "https://" + DEFAULT_API_HOST;

    private final ClientFactory clientFactory;

    public AbstractApiFactory()
    {
        this(new ClientFactory());
    }

    protected AbstractApiFactory(final ClientFactory clientFactory)
    {
        this.clientFactory = clientFactory;
    }

    protected abstract Class<T> getApiClass();

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

    public T buildApi(final String userIdentifier, final String userSecret)
    {
        Objects.requireNonNull(userIdentifier, "userIdentifier must be defined");
        Objects.requireNonNull(userSecret, "userSecret must be defined");

        final BearerAuthSecretFilter bearerAuthSecretFilter = new BearerAuthSecretFilter(userIdentifier, userSecret, DEFAULT_API_HOST);
        return buildApi(bearerAuthSecretFilter);
    }

    public T buildApi(final AbstractBearerAuthFilter authFilter)
    {
        return buildApi(authFilter, DEFAULT_API_HOST_AND_PROTOCOL);
    }

    public T buildApi(final AbstractBearerAuthFilter authFilter, final String hostAndProtocol)
    {
        Objects.requireNonNull(authFilter, "authFilter must be defined");

        final List<ClientRequestFilter> filters = new LinkedList<>();
        filters.add(authFilter);

        return buildApi(filters, hostAndProtocol);
    }

    public T buildApi(final List<ClientRequestFilter> filterList, final String hostAndProtocol)
    {
        return buildApi(filterList, hostAndProtocol, getHttpClientConfiguration());
    }

    public T buildApi(final List<ClientRequestFilter> filterList, final String hostAndProtocol, final HttpClientConfiguration httpClientConfiguration)
    {
        Objects.requireNonNull(filterList, "filterList must be defined");
        Objects.requireNonNull(hostAndProtocol, "hostAndProtocol must be defined");
        Objects.requireNonNull(httpClientConfiguration, "httpClientConfiguration must be defined");

        return clientFactory.build(filterList, getClientResponseFilters(), hostAndProtocol, getApiClass(), getDeserializerMap(), httpClientConfiguration, null);
    }


    public T buildApi(final List<ClientRequestFilter> filterList, final String hostAndProtocol, final ResteasyProviderFactory providerFactory)
    {
        Objects.requireNonNull(filterList, "filterList must be defined");
        Objects.requireNonNull(hostAndProtocol, "hostAndProtocol must be defined");

        return clientFactory.build(filterList, getClientResponseFilters(), hostAndProtocol, getApiClass(), getDeserializerMap(), getHttpClientConfiguration(), providerFactory);
    }

    public T buildApi(final List<ClientRequestFilter> filterList, final String hostAndProtocol, final HttpClientConfiguration httpClientConfiguration, final ResteasyProviderFactory providerFactory)
    {
        Objects.requireNonNull(filterList, "filterList must be defined");
        Objects.requireNonNull(hostAndProtocol, "hostAndProtocol must be defined");
        Objects.requireNonNull(httpClientConfiguration, "httpClientConfiguration must be defined");

        return clientFactory.build(filterList, getClientResponseFilters(), hostAndProtocol, getApiClass(), getDeserializerMap(), httpClientConfiguration, providerFactory);
    }
}
