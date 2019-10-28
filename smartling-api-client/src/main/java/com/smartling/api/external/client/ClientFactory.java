package com.smartling.api.external.client;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartling.api.external.client.exception.RestApiExceptionHandler;
import com.smartling.api.external.client.proxy.ExceptionDecoratorInvocationHandler;
import com.smartling.api.external.client.unmarshal.DetailsDeserializer;
import com.smartling.api.external.client.unmarshal.RestApiContextResolver;
import com.smartling.api.external.client.unmarshal.RestApiResponseReaderInterceptor;
import com.smartling.web.api.v2.Details;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.ClientErrorInterceptor;
import org.jboss.resteasy.client.jaxrs.ClientHttpEngine;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.factory.ApacheHttpClient4EngineFactory;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.ContextResolver;
import java.lang.reflect.Proxy;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Base factory for building proxied client objects
 */
public class ClientFactory
{
    protected static final String TLS_ENVIRONMENT_VAR = "SMARTLING_TLS_ENABLED";
    protected static final String TLS_PROPERTY = "com.smartling.tls.enabled";

    protected boolean enableSslSecurity()
    {
        boolean envTls = true;
        boolean propTls = true;

        if (System.getenv(TLS_ENVIRONMENT_VAR) != null)
        {
            envTls = Boolean.valueOf(System.getenv(TLS_ENVIRONMENT_VAR));
        }

        if (System.getProperty(TLS_PROPERTY) != null)
        {
            propTls = Boolean.valueOf(System.getProperty(TLS_PROPERTY));
        }

        return (envTls && propTls);
    }

    protected HttpClientConnectionManager getHttpClientConnectionManager(final HttpClientConfiguration configuration)
    {
        final SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .setSoKeepAlive(true)
                .build();
        final PoolingHttpClientConnectionManager connectionManager;

        if (enableSslSecurity())
            connectionManager = new PoolingHttpClientConnectionManager();
        else
        {
            try
            {
                final SSLContext sslContext = new SSLContextBuilder().setSecureRandom(new SecureRandom()).loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
                final X509HostnameVerifier verifier = new AllowAllHostnameVerifier();
                final LayeredConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, verifier);
                final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", sslSocketFactory)
                        .build();

                connectionManager = new PoolingHttpClientConnectionManager(registry);
            }
            catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException ex)
            {
                throw new IllegalStateException("Failed to create SSL context", ex);
            }
        }

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
    public <T> T build(final List<ClientRequestFilter> clientRequestFilters, final List<ClientResponseFilter> clientResponseFilters, final String domain, final Class<T> klass, final Map<Class<?>, JsonDeserializer<?>> deserializerMap,
            final HttpClientConfiguration configuration, final ResteasyProviderFactory providerFactory)
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

        if (!enableSslSecurity())
            builder.disableTrustManager();

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
