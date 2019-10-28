package com.smartling.api.external.client.graphql;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartling.api.external.client.ClientFactory;
import com.smartling.api.external.client.HttpClientConfiguration;
import com.smartling.api.external.client.exception.RestApiExceptionHandler;
import com.smartling.api.external.client.proxy.ExceptionDecoratorInvocationHandler;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.ContextResolver;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GraphQlClientFactory extends ClientFactory
{
    private Map<Class<?>, JsonSerializer<?>> serializationMap()
    {
        final Map<Class<?>, JsonSerializer<?>> serializationMap = new HashMap<>();

        serializationMap.put(AbstractGraphQlModel.class, new GraphQlSerializer());

        return serializationMap;
    }

    @Override
    protected ContextResolver<ObjectMapper> getObjectMapperContextResolver(Map<Class<?>, JsonDeserializer<?>> classJsonDeserializerMap)
    {
        return new GraphQlContextResolver(classJsonDeserializerMap, serializationMap());
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
                .register(new GraphQlResponseReaderInterceptor())
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
