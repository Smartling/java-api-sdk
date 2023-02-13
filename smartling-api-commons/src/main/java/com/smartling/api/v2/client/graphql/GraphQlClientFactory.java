package com.smartling.api.v2.client.graphql;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.smartling.api.v2.client.ClientFactory;

import javax.ws.rs.ext.ReaderInterceptor;
import java.util.HashMap;
import java.util.Map;

public class GraphQlClientFactory extends ClientFactory
{
    @Override
    protected Map<Class<?>, JsonSerializer<?>> getSerializerMap()
    {
        final Map<Class<?>, JsonSerializer<?>> serializationMap = new HashMap<>();

        serializationMap.put(AbstractGraphQlModel.class, new GraphQlSerializer());

        return serializationMap;
    }

    @Override
    protected ReaderInterceptor getRestApiResponseReaderInterceptor()
    {
        return new GraphQlResponseReaderInterceptor();
    }
}
