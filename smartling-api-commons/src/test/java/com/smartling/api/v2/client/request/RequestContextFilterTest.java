package com.smartling.api.v2.client.request;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.junit.After;
import org.junit.Test;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestContextFilterTest
{
    private final RequestContextFilter filter = new RequestContextFilter();

    @After
    public void tearDown()
    {
        RequestContextHolder.clearContext();
    }

    @Test
    public void shouldSetRequestContextFromClientRequestContext() throws Exception
    {
        // Given
        final ClientRequestContext clientRequestContext = mock(ClientRequestContext.class);
        final MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<>();
        headers.add("Content-Type", "application/json");

        when(clientRequestContext.getMethod()).thenReturn("POST");
        when(clientRequestContext.getUri()).thenReturn(URI.create("https://api.smartling.com/test"));
        when(clientRequestContext.getHeaders()).thenReturn(headers);

        // When
        filter.filter(clientRequestContext);

        // Then
        RequestContext requestContext = RequestContextHolder.getContext();
        assertNotNull(requestContext);
        assertEquals("POST", requestContext.getMethod());
        assertEquals(URI.create("https://api.smartling.com/test"), requestContext.getUri());
        assertEquals("application/json", requestContext.getHeaders().getFirst("Content-Type"));
    }
}
