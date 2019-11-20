package com.smartling.api.v2.client;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartling.api.v2.client.exception.RestApiExceptionHandler;
import com.smartling.api.v2.client.proxy.ExceptionDecoratorInvocationHandler;
import com.smartling.api.v2.client.unmarshal.DetailsDeserializer;
import com.smartling.api.v2.client.unmarshal.RestApiContextResolver;
import com.smartling.api.v2.client.unmarshal.RestApiResponseReaderInterceptor;
import com.smartling.api.v2.response.Details;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jboss.resteasy.client.jaxrs.ClientHttpEngine;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.factory.ApacheHttpClient4EngineFactory;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.ContextResolver;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Client factory for building JAX-RS proxied client APIs.
 */
public class ClientFactory
{
    protected HttpClientConnectionManager getHttpClientConnectionManager(final HttpClientConfiguration configuration)
    {
        final SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .setSoKeepAlive(true)
                .build();
        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

        connectionManager.setDefaultMaxPerRoute(configuration.getMaxThreadPerRoute());
        connectionManager.setDefaultSocketConfig(socketConfig);
        connectionManager.setMaxTotal(configuration.getMaxThreadTotal());

        return connectionManager;
    }

    protected RequestConfig getRequestConfig(final HttpClientConfiguration configuration)
    {
        return RequestConfig.custom()
                .setSocketTimeout(configuration.getSocketTimeout())
                .setConnectTimeout(configuration.getConnectionTimeout())
                .setConnectionRequestTimeout(configuration.getConnectionRequestTimeout())
                .setStaleConnectionCheckEnabled(configuration.isStaleConnectionCheckEnabled())
                .build();
    }

    protected HttpClientBuilder getHttpClientBuilder(final HttpClientConfiguration configuration)
    {
        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setDefaultRequestConfig(getRequestConfig(configuration))
                .setConnectionManager(getHttpClientConnectionManager(configuration));

        if (configuration.getProxyHost() != null && configuration.getProxyPort() != null)
        {
            HttpHost proxyHost = new HttpHost(configuration.getProxyHost(), configuration.getProxyPort());
            httpClientBuilder.setProxy(proxyHost);

            if (configuration.getProxyUser() != null && configuration.getProxyPassword() != null)
            {
                BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(
                        new AuthScope(proxyHost),
                        new UsernamePasswordCredentials(configuration.getProxyUser(), configuration.getProxyPassword())
                );
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
        }

        return httpClientBuilder;
    }

    protected ClientHttpEngine getClientHttpEngine(final HttpClientConfiguration configuration)
    {
        final HttpClientBuilder httpClientBuilder = getHttpClientBuilder(configuration);
        final CloseableHttpClient httpClient = httpClientBuilder.build();
        return ApacheHttpClient4EngineFactory.create(httpClient, true);
    }

    protected Map<Class<?>, JsonDeserializer<?>> getDefaultDeserializerMap()
    {
        final Map<Class<?>, JsonDeserializer<?>> classJsonDeserializerMap = new HashMap<>();
        classJsonDeserializerMap.put(Details.class, new DetailsDeserializer());
        return classJsonDeserializerMap;
    }

    protected ContextResolver<ObjectMapper> getObjectMapperContextResolver(final Map<Class<?>, JsonDeserializer<?>> classJsonDeserializerMap)
    {
        return new RestApiContextResolver(classJsonDeserializerMap);
    }

    public <T> T build(final List<ClientRequestFilter> clientRequestFilters, final List<ClientResponseFilter> clientResponseFilters, final String domain, final Class<T> klass)
    {
        return build(clientRequestFilters, clientResponseFilters, domain, klass, getDefaultDeserializerMap(), new HttpClientConfiguration(), null);
    }

    @SuppressWarnings("unchecked")
    <T> T build(final List<ClientRequestFilter> clientRequestFilters, final List<ClientResponseFilter> clientResponseFilters, final String domain,
        final Class<T> klass, final Map<Class<?>, JsonDeserializer<?>> deserializerMap, final HttpClientConfiguration configuration,
        final ResteasyProviderFactory providerFactory)
    {
        Objects.requireNonNull(clientRequestFilters, "clientRequestFilters must be defined");
        Objects.requireNonNull(clientResponseFilters, "clientResponseFilters must be defined");
        Objects.requireNonNull(domain, "domain must be defined");
        Objects.requireNonNull(klass, "klass must be defined");
        Objects.requireNonNull(deserializerMap, "deserializerMap must be defined");
        Objects.requireNonNull(configuration, "configuration must be defined");

        if (clientRequestFilters.isEmpty())
            throw new IllegalArgumentException("At least one request filter is required for authorization");

        final ResteasyClientBuilder builder = new ResteasyClientBuilder();
        builder.httpEngine(getClientHttpEngine(configuration));

        if (providerFactory != null)
            builder.providerFactory(providerFactory);

        final ContextResolver<ObjectMapper> contextResolver = getObjectMapperContextResolver(deserializerMap);

        final ResteasyWebTarget client = builder.build()
                .target(domain)
                .register(new RestApiResponseReaderInterceptor())
                .register(contextResolver);

        for (final ClientRequestFilter filter : clientRequestFilters)
            client.register(filter);

        for (final ClientResponseFilter filter : clientResponseFilters)
            client.register(filter);

        final T proxy = client.proxy(klass);

        final RestApiExceptionHandler exceptionHandler = new RestApiExceptionHandler();
        final ExceptionDecoratorInvocationHandler<T> handler = new ExceptionDecoratorInvocationHandler<>(proxy, exceptionHandler);

        return (T)Proxy.newProxyInstance(klass.getClassLoader(), new Class[] {klass}, handler);
    }
}
