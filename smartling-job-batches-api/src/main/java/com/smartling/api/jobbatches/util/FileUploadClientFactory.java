package com.smartling.api.jobbatches.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartling.api.v2.client.ClientFactory;
import com.smartling.api.v2.client.HttpClientConfiguration;
import com.smartling.api.v2.client.unmarshal.RestApiResponseReaderInterceptor;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.ContextResolver;
import java.util.List;
import java.util.Objects;

public class FileUploadClientFactory extends ClientFactory
{
    public ResteasyClient build(HttpClientConfiguration configuration)
    {
        Objects.requireNonNull(configuration, "configuration must be defined");

        ResteasyClientBuilder builder = ((ResteasyClientBuilder) ClientBuilder.newBuilder());
        builder.httpEngine(super.getClientHttpEngine(configuration));

        return builder.build();
    }

    public ResteasyWebTarget build(ResteasyClient resteasyClient, List<ClientRequestFilter> clientRequestFilters, String domain)
    {
        Objects.requireNonNull(resteasyClient, "resteasy client must be defined");
        Objects.requireNonNull(clientRequestFilters, "clientRequestFilters must be defined");
        Objects.requireNonNull(domain, "domain must be defined");

        if (!containsAuthFilter(clientRequestFilters))
        {
            throw new IllegalArgumentException("At least one request filter is required for authorization");
        }

        ContextResolver<ObjectMapper> contextResolver = super.getObjectMapperContextResolver(super.getDeserializerMap(), super.getSerializerMap());
        ResteasyWebTarget client = resteasyClient.target(domain).register(new RestApiResponseReaderInterceptor()).register(contextResolver);

        for (ClientRequestFilter filter : clientRequestFilters)
        {
            client.register(filter);
        }

        return client;
    }
}
