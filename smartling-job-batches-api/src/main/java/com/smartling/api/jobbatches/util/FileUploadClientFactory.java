package com.smartling.api.jobbatches.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartling.api.v2.client.ClientFactory;
import com.smartling.api.v2.client.HttpClientConfiguration;
import com.smartling.api.v2.client.unmarshal.RestApiResponseReaderInterceptor;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.ContextResolver;
import java.util.List;
import java.util.Objects;

public class FileUploadClientFactory extends ClientFactory
{
    public ResteasyWebTarget build(List<ClientRequestFilter> clientRequestFilters, String domain, HttpClientConfiguration configuration)
    {
        Objects.requireNonNull(clientRequestFilters, "clientRequestFilters must be defined");
        Objects.requireNonNull(domain, "domain must be defined");
        Objects.requireNonNull(configuration, "configuration must be defined");
        if (clientRequestFilters.isEmpty())
        {
            throw new IllegalArgumentException("At least one request filter is required for authorization");
        }

        ResteasyClientBuilder builder = new ResteasyClientBuilder();
        builder.httpEngine(super.getClientHttpEngine(configuration));

        ContextResolver<ObjectMapper> contextResolver = super.getObjectMapperContextResolver(super.getDefaultDeserializerMap());
        ResteasyWebTarget client = builder.build().target(domain).register(new RestApiResponseReaderInterceptor()).register(contextResolver);

        for (ClientRequestFilter filter : clientRequestFilters)
        {
            client.register(filter);
        }

        return client;
    }
}
