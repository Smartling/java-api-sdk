package com.smartling.api.v2.client.unmarshal;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartling.api.sdk.v2.response.Details;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RestApiContextResolverTest
{
    @Test
    public void testGetContext() throws Exception
    {
        final Map<Class<?>, JsonDeserializer<?>> classJsonDeserializerMap = spy(new HashMap<Class<?>, JsonDeserializer<?>>());
        classJsonDeserializerMap.put(Details.class, new DetailsDeserializer());

        final RestApiContextResolver resolver = new RestApiContextResolver(classJsonDeserializerMap);
        final ObjectMapper objectMapper = resolver.getContext(ObjectMapper.class);
        assertFalse(objectMapper.getDeserializationConfig().isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
        verify(classJsonDeserializerMap, times(1)).get(Details.class);
    }
}
