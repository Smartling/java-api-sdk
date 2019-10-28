package com.smartling.api.external.client.graphql;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.util.Map;

@Provider
@Produces({"application/json"})
public class GraphQlContextResolver implements ContextResolver<ObjectMapper>
{
    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    public GraphQlContextResolver(final Map<Class<?>, JsonDeserializer<?>> classJsonDeserializerMap,
                                  final Map<Class<?>, JsonSerializer<?>> classJsonSerializerMap)
    {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SimpleModule module = new SimpleModule();
        for (final Class<?> klass : classJsonDeserializerMap.keySet())
            module.addDeserializer(klass, (JsonDeserializer)classJsonDeserializerMap.get(klass));

        for (final Class<?> klass : classJsonSerializerMap.keySet())
            module.addSerializer(klass, (JsonSerializer) classJsonSerializerMap.get(klass));

        objectMapper.registerModule(module);

        this.objectMapper = objectMapper;
    }

    @Override
    public ObjectMapper getContext(final Class<?> type)
    {
        return objectMapper;
    }
}
