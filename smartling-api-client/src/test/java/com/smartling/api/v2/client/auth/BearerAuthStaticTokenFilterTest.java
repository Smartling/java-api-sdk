package com.smartling.api.v2.client.auth;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class BearerAuthStaticTokenFilterTest
{
    private static final String TOKEN       = "foo";
    private static final String AUTH_HEADER = "Bearer foo";

    private BearerAuthStaticTokenFilter filter;

    @Before
    public void setUp() throws Exception
    {
        filter = new BearerAuthStaticTokenFilter(TOKEN);
    }

    @Test
    public void testGetTokenStringNull() throws Exception
    {
        filter = new BearerAuthStaticTokenFilter(null);
        assertNull(filter.getTokenString());

        final ClientRequestContext requestContext = mock(ClientRequestContext.class);
        when(requestContext.getHeaders()).thenReturn(mock(MultivaluedMap.class));
        filter.filter(requestContext);

        verify(requestContext.getHeaders(), never()).add(eq(HttpHeaders.AUTHORIZATION), any(String.class));
    }

    @Test
    public void testGetTokenString() throws Exception
    {
        assertEquals(TOKEN, filter.getTokenString());
        final ClientRequestContext requestContext = mock(ClientRequestContext.class);
        when(requestContext.getHeaders()).thenReturn(mock(MultivaluedMap.class));
        filter.filter(requestContext);
        verify(requestContext.getHeaders(), times(1)).add(HttpHeaders.AUTHORIZATION, AUTH_HEADER);
    }
}
