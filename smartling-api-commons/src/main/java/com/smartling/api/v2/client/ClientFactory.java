package com.smartling.api.v2.client;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartling.api.v2.client.auth.AuthorizationRequestFilter;
import com.smartling.api.v2.client.exception.DefaultRestApiExceptionMapper;
import com.smartling.api.v2.client.exception.RestApiExceptionHandler;
import com.smartling.api.v2.client.exception.RestApiExceptionMapper;
import com.smartling.api.v2.client.request.RequestContextFilter;
import com.smartling.api.v2.client.request.RequestContextInvocationHandler;
import com.smartling.api.v2.client.unmarshal.DetailsDeserializer;
import com.smartling.api.v2.client.unmarshal.RestApiContextResolver;
import com.smartling.api.v2.client.unmarshal.RestApiResponseReaderInterceptor;
import com.smartling.api.v2.response.Details;
import com.smartling.resteasy.ext.ExtendedMultipartFormWriter;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.jboss.resteasy.client.jaxrs.ClientHttpEngine;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.ReaderInterceptor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Client factory for building JAX-RS proxied client APIs.
 */
public class ClientFactory
{
    /**
     * Returns true if the given client request filter list contains an
     * authorization filter.
     *
     * @param clientRequestFilters the <code>List</code> of <code>ClientRequestFilter</code>s to check
     * @return <code>true</code> if the given <code>clientRequestFilters</code> contains an instance of
     * {@link AuthorizationRequestFilter}; <code>false</code> otherwise
     */
    protected static boolean containsAuthFilter(List<ClientRequestFilter> clientRequestFilters)
    {
        if (clientRequestFilters == null)
            return false;

        for (ClientRequestFilter filter : clientRequestFilters)
        {
            if (filter instanceof AuthorizationRequestFilter)
            {
                return true;
            }
        }

        return false;
    }

    private ConnectionSocketFactory getSslConnectionSocketFactory(final HttpClientConfiguration configuration)
    {
        SSLContext sslContext = configuration.getSslContext() != null ? configuration.getSslContext() : SSLContexts.createDefault();
        return new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
    }

    /**
     * Returns the HTTP client connection manager to use for API requests.
     *
     * @param configuration the <code>HttpClientConfiguration</code> for the
     *                      <code>HttpClientConnectionManager</code>
     * @return a configured <code>HttpClientConnectionManager</code>
     */
    protected HttpClientConnectionManager getHttpClientConnectionManager(final HttpClientConfiguration configuration)
    {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.getSocketFactory())
            .register("https", getSslConnectionSocketFactory(configuration))
            .build());

        connectionManager.setDefaultMaxPerRoute(configuration.getMaxThreadPerRoute());
        connectionManager.setDefaultSocketConfig(SocketConfig.custom()
            .setTcpNoDelay(true)
            .setSoKeepAlive(true)
            .build());
        connectionManager.setMaxTotal(configuration.getMaxThreadTotal());

        return connectionManager;
    }

    /**
     * Returns the request configuration to use for API requests.
     *
     * @param configuration the <code>HttpClientConfiguration</code> for the <code>RequestConfig</code>
     * @return a configured <code>RequestConfig</code>
     */
    protected RequestConfig getRequestConfig(final HttpClientConfiguration configuration)
    {
        return RequestConfig.custom()
            .setSocketTimeout(configuration.getSocketTimeout())
            .setConnectTimeout(configuration.getConnectionTimeout())
            .setConnectionRequestTimeout(configuration.getConnectionRequestTimeout())
            .setStaleConnectionCheckEnabled(configuration.isStaleConnectionCheckEnabled())
            .build();
    }

    /**
     * Returns an HTTP client builder.
     *
     * @param configuration the <code>HttpClientConfiguration</code> for the <code>HttpClientBuilder</code>
     * @return a configured <code>HttpClientBuilder</code>
     */
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

    /**
     * Returns the client HTTP engine for API requests.
     *
     * @param configuration the <code>HttpClientConfiguration</code> for the <code>ClientHttpEngine</code>
     * @return a configured <code>HttpClientEngine</code>
     */
    protected ClientHttpEngine getClientHttpEngine(final HttpClientConfiguration configuration)
    {
        final HttpClientBuilder httpClientBuilder = getHttpClientBuilder(configuration);
        final CloseableHttpClient httpClient = httpClientBuilder.build();
        return new ApacheHttpClient43Engine(httpClient, true);
    }

    /**
     * Returns the JSON deserializer map for unmarshalling API responses.
     *
     * @return a <code>Map</code> of classes to custom <code>JsonDeserializer</code>s
     */
    protected Map<Class<?>, JsonDeserializer<?>> getDeserializerMap()
    {
        final Map<Class<?>, JsonDeserializer<?>> classJsonDeserializerMap = new HashMap<>();
        classJsonDeserializerMap.put(Details.class, new DetailsDeserializer());
        return classJsonDeserializerMap;
    }

    protected Map<Class<?>, JsonSerializer<?>> getSerializerMap()
    {
        return Collections.emptyMap();
    }

    protected ContextResolver<ObjectMapper> getObjectMapperContextResolver(final Map<Class<?>, JsonDeserializer<?>> classJsonDeserializerMap,
                                                                           final Map<Class<?>, JsonSerializer<?>> classJsonSerializerMap)
    {
        return new RestApiContextResolver(classJsonDeserializerMap, classJsonSerializerMap);
    }

    protected ReaderInterceptor getRestApiResponseReaderInterceptor()
    {
        return new RestApiResponseReaderInterceptor();
    }

    public <T> T build(final List<ClientRequestFilter> clientRequestFilters, final List<ClientResponseFilter> clientResponseFilters, final String domain,
                       final Class<T> klass)
    {
        return build(clientRequestFilters, clientResponseFilters, domain, klass, new HttpClientConfiguration(), null, null);
    }

    /**
     * Returns a fully configured JAX-RS proxy for an API of type <code>T</code>
     *
     * @param clientRequestFilters  the <code>ClientRequestFilters</code> (required)
     * @param clientResponseFilters the <code>ClientResponseFilters</code> (required)
     * @param domain                the API protocol and domain (required)
     * @param klass                 the <code>Class</code> of type <code>T</code>
     * @param configuration         the <code>HttpClientConfiguration</code> (required)
     * @param providerFactory       the <code>ResteasyProviderFactory</code>
     * @param exceptionMapper       the <code>RestApiExceptionMapper</code>
     * @return a full configured JAX-RS proxy for <code>T</code>
     */
    @SuppressWarnings("unchecked")
    <T> T build(
        final List<ClientRequestFilter> clientRequestFilters,
        final List<ClientResponseFilter> clientResponseFilters,
        final String domain,
        final Class<T> klass,
        final HttpClientConfiguration configuration,
        final ResteasyProviderFactory providerFactory,
        final RestApiExceptionMapper exceptionMapper)
    {
        Objects.requireNonNull(clientRequestFilters, "clientRequestFilters must be defined");
        Objects.requireNonNull(clientResponseFilters, "clientResponseFilters must be defined");
        Objects.requireNonNull(domain, "domain must be defined");
        Objects.requireNonNull(klass, "klass must be defined");
        Objects.requireNonNull(this.getDeserializerMap(), "deserializerMap must be defined");
        Objects.requireNonNull(configuration, "configuration must be defined");

        if (!containsAuthFilter(clientRequestFilters))
            throw new IllegalArgumentException("At least one request filter is required for authorization");

        final ResteasyClientBuilder builder = ((ResteasyClientBuilder) ClientBuilder.newBuilder());
        builder.httpEngine(getClientHttpEngine(configuration));

        if (providerFactory != null)
            builder.providerFactory(providerFactory);

        final ContextResolver<ObjectMapper> contextResolver = getObjectMapperContextResolver(getDeserializerMap(), getSerializerMap());

        final ResteasyWebTarget client = builder.build()
            .target(domain)
            .register(getRestApiResponseReaderInterceptor())
            .register(new ExtendedMultipartFormWriter())
            .register(contextResolver);

        for (final ClientRequestFilter filter : clientRequestFilters)
            client.register(filter);

        for (final ClientResponseFilter filter : clientResponseFilters)
            client.register(filter);

        client.register(new DefaultRequestIdFilter());
        client.register(new RequestContextFilter());

        final T proxy = client.proxy(klass);
        final RestApiExceptionHandler exceptionHandler = new RestApiExceptionHandler(exceptionMapper != null ? exceptionMapper : new DefaultRestApiExceptionMapper());
        InvocationHandler handler = new ExceptionDecoratorInvocationHandler<>(proxy, exceptionHandler);
        handler = new CloseClientInvocationHandler(handler, client.getResteasyClient());
        handler = new RequestContextInvocationHandler(handler);

        return (T) Proxy.newProxyInstance(klass.getClassLoader(), new Class[] { klass }, handler);
    }
}
