package com.smartling.api.v2.client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedMap;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultRequestIdFilterTest
{
    private static final String REQUEST_ID_HEADER = "X-SL-RequestId";

    @Mock
    private ClientRequestContext requestContext;

    @Mock
    private MultivaluedMap<String, Object> headers;

    private final DefaultRequestIdFilter filter = new DefaultRequestIdFilter();

    @Before
    public void setUp()
    {
        when(requestContext.getHeaders()).thenReturn(headers);
    }

    @Test
    public void shouldNotAddHeaderWhenRequestIdHeaderExists() throws Exception
    {
        // Given
        when(requestContext.getHeaderString(REQUEST_ID_HEADER)).thenReturn("existing-request-id");

        // When
        filter.filter(requestContext);

        // Then
        verifyNoMoreInteractions(headers);
    }

    @Test
    public void shouldAddHeaderWhenRequestIdHeaderDoesNotExist() throws Exception
    {
        // Given
        when(requestContext.getHeaderString(REQUEST_ID_HEADER)).thenReturn(null);

        // When
        filter.filter(requestContext);

        // Then
        verify(headers).addFirst(eq(REQUEST_ID_HEADER), any());
        verifyNoMoreInteractions(headers);
    }

    @Test
    public void shouldAddHeaderWhenRequestIdHeaderIsEmpty() throws Exception
    {
        // Given
        when(requestContext.getHeaderString(REQUEST_ID_HEADER)).thenReturn("");

        // When
        filter.filter(requestContext);

        // Then
        verify(headers).addFirst(eq(REQUEST_ID_HEADER), any());
        verifyNoMoreInteractions(headers);
    }

    @Test
    public void shouldAddHeaderWhenRequestIdHeaderIsWhitespace() throws Exception
    {
        // Given
        when(requestContext.getHeaderString(REQUEST_ID_HEADER)).thenReturn("   ");

        // When
        filter.filter(requestContext);

        // Then
        verify(headers).addFirst(eq(REQUEST_ID_HEADER), any());
        verifyNoMoreInteractions(headers);
    }
}
