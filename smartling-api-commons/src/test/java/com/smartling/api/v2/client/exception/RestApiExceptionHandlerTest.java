package com.smartling.api.v2.client.exception;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.smartling.api.v2.response.ErrorResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class RestApiExceptionHandlerTest
{
    private static final int FAILURE_STATUS = 500;

    @Mock
    private RestApiExceptionMapper exceptionMapper;
    @Mock
    private Exception cause;
    @Mock
    private RestApiRuntimeException restApiRuntimeException;
    @Mock
    private Response response;

    private RestApiExceptionHandler handler;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        when(exceptionMapper.toException((Throwable) any(), (Response) any(), (ErrorResponse) any()))
            .thenReturn(restApiRuntimeException);
        when(restApiRuntimeException.getStatus()).thenReturn(FAILURE_STATUS);
        when(restApiRuntimeException.getCause()).thenReturn(cause);
        when(response.getStatusInfo()).thenReturn(mock(Response.StatusType.class));
        handler = new RestApiExceptionHandler(exceptionMapper);
    }

    @Test
    public void testCreateRestApiExceptionRuntimeException() throws Exception
    {
        final RuntimeException ex = new RuntimeException();

        final RestApiRuntimeException restApiRuntimeException = handler.createRestApiException(ex);
        assertEquals(ex, restApiRuntimeException.getCause());
        assertEquals(500, restApiRuntimeException.getStatus());
        assertEquals(Collections.emptyList(), restApiRuntimeException.getErrors());
        verify(exceptionMapper, never()).toException(any(Throwable.class), any(Response.class), any(ErrorResponse.class));
    }

    @Test
    public void testCreateRestApiExceptionInvocationTargetException() throws Exception
    {
        final RuntimeException ex = new RuntimeException();
        final InvocationTargetException invocationTargetException = new InvocationTargetException(ex);

        final RestApiRuntimeException restApiRuntimeException = handler.createRestApiException(invocationTargetException);
        assertEquals(ex, restApiRuntimeException.getCause());
        assertEquals(500, restApiRuntimeException.getStatus());
        assertEquals(Collections.emptyList(), restApiRuntimeException.getErrors());
        verify(exceptionMapper, never()).toException(any(Throwable.class), any(Response.class), any(ErrorResponse.class));
    }

    @Test
    public void testCreateRestApiExceptionWebApplicationExceptionNullResponse() throws Exception
    {
        final WebApplicationException ex = new WebApplicationException((Response)null);
        final InvocationTargetException invocationTargetException = new InvocationTargetException(ex);

        final RestApiRuntimeException restApiRuntimeException = handler.createRestApiException(invocationTargetException);
        assertEquals(cause, restApiRuntimeException.getCause());
        assertEquals(500, restApiRuntimeException.getStatus());
        assertEquals(Collections.emptyList(), restApiRuntimeException.getErrors());
        verify(exceptionMapper, times(1)).toException(eq(ex), any(Response.class), (ErrorResponse)isNull());
    }

    @Test
    public void testCreateRestApiExceptionWebApplicationException() throws Exception
    {
        final WebApplicationException ex = new WebApplicationException(response);
        final InvocationTargetException invocationTargetException = new InvocationTargetException(ex);

        final RestApiRuntimeException restApiRuntimeException = handler.createRestApiException(invocationTargetException);
        assertEquals(cause, restApiRuntimeException.getCause());
        assertEquals(FAILURE_STATUS, restApiRuntimeException.getStatus());
        assertEquals(Collections.emptyList(), restApiRuntimeException.getErrors());
        verify(exceptionMapper, times(1)).toException(eq(ex), eq(response), (ErrorResponse)isNull());
    }

    @Test
    public void testCreateRestApiExceptionWebApplicationExceptionWithErrorsEntity() throws Exception
    {
        final ErrorResponse errorResponse = mock(ErrorResponse.class);
        when(response.readEntity(ErrorResponse.class)).thenReturn(errorResponse);

        final WebApplicationException ex = new WebApplicationException(response);
        final InvocationTargetException invocationTargetException = new InvocationTargetException(ex);

        final RestApiRuntimeException restApiRuntimeException = handler.createRestApiException(invocationTargetException);
        assertEquals(cause, restApiRuntimeException.getCause());
        assertEquals(FAILURE_STATUS, restApiRuntimeException.getStatus());
        assertEquals(Collections.emptyList(), restApiRuntimeException.getErrors());
        verify(exceptionMapper, times(1)).toException(eq(ex), eq(response), eq(errorResponse));
    }

    @Test
    public void testCreateRestApiExceptionWebApplicationExceptionIllegalStateException() throws Exception
    {
        final ErrorResponse errorResponse = mock(ErrorResponse.class);
        when(response.readEntity(ErrorResponse.class)).thenThrow(new IllegalStateException());

        final WebApplicationException ex = new WebApplicationException(response);
        final InvocationTargetException invocationTargetException = new InvocationTargetException(ex);

        final RestApiRuntimeException restApiRuntimeException = handler.createRestApiException(invocationTargetException);
        verify(exceptionMapper, times(1)).toException(eq(ex), eq(response), (ErrorResponse)isNull());
    }

    @Test
    public void testCreateRestApiExceptionWebApplicationExceptionProcessingException() throws Exception
    {
        final ErrorResponse errorResponse = mock(ErrorResponse.class);
        when(response.readEntity(ErrorResponse.class)).thenThrow(new ProcessingException("foo"));

        final WebApplicationException ex = new WebApplicationException(response);
        final InvocationTargetException invocationTargetException = new InvocationTargetException(ex);

        final RestApiRuntimeException restApiRuntimeException = handler.createRestApiException(invocationTargetException);
        verify(exceptionMapper, times(1)).toException(eq(ex), eq(response), (ErrorResponse)isNull());
    }
}
