package com.smartling.api.v2.client.unmarshal;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartling.api.v2.client.graphql.AbstractGraphQlModel;
import com.smartling.api.v2.client.graphql.GraphQlSerializer;
import com.smartling.api.v2.response.Details;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RestApiContextResolverTest
{
    @Test
    public void testGetContext()
    {
        final Map<Class<?>, JsonDeserializer<?>> classJsonDeserializerMap = spy(new HashMap<>());
        classJsonDeserializerMap.put(Details.class, new DetailsDeserializer());

        final Map<Class<?>, JsonSerializer<?>> classJsonSerializerMap = spy(new HashMap<>());
        classJsonSerializerMap.put(AbstractGraphQlModel.class, new GraphQlSerializer());

        final RestApiContextResolver resolver = new RestApiContextResolver(classJsonDeserializerMap, classJsonSerializerMap);
        final ObjectMapper objectMapper = resolver.getContext(ObjectMapper.class);
        assertFalse(objectMapper.getDeserializationConfig().isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
        verify(classJsonDeserializerMap, times(1)).get(Details.class);
        verify(classJsonSerializerMap, times(1)).get(AbstractGraphQlModel.class);
    }
}
