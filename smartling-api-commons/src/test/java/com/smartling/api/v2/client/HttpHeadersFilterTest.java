package com.smartling.api.v2.client;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HttpHeadersFilterTest
{
    private static final String HTTP_HEADER_KEY = "http_header_key";
    private static final String HTTP_HEADER_VALUE = "http_header_value";

    @Test
    public void shouldSetHttpHeadersWithMap() throws Exception
    {
        Map<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put(HTTP_HEADER_KEY, HTTP_HEADER_VALUE);
        final HttpHeadersFilter httpHeadersFilter = new HttpHeadersFilter(httpHeaders);
        final ClientRequestContext requestContext = mock(ClientRequestContext.class);
        when(requestContext.getHeaders()).thenReturn(mock(MultivaluedMap.class));

        httpHeadersFilter.filter(requestContext);

        verify(requestContext.getHeaders(), times(1)).add(eq(HTTP_HEADER_KEY), eq(HTTP_HEADER_VALUE));
    }

    @Test
    public void shouldSetHttpHeadersWithValues() throws Exception
    {
        final HttpHeadersFilter httpHeadersFilter = new HttpHeadersFilter(HTTP_HEADER_KEY, HTTP_HEADER_VALUE);
        final ClientRequestContext requestContext = mock(ClientRequestContext.class);
        when(requestContext.getHeaders()).thenReturn(mock(MultivaluedMap.class));

        httpHeadersFilter.filter(requestContext);

        verify(requestContext.getHeaders(), times(1)).add(eq(HTTP_HEADER_KEY), eq(HTTP_HEADER_VALUE));
    }

}
