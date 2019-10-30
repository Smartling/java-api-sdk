package com.smartling.api.external.client.exception;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class RestApiExceptionHandlerTest
{
    private static final int FAILURE_STATUS = 400;

    private RestApiExceptionHandler handler;

    @Before
    public void setUp() throws Exception
    {
        handler = new RestApiExceptionHandler();
    }

    @Test
    public void testCreateRestApiExceptionRuntimeException() throws Exception
    {
        final RuntimeException ex = new RuntimeException();

        final RestApiRuntimeException restApiRuntimeException = handler.createRestApiException(ex);
        assertEquals(ex, restApiRuntimeException.getCause());
        assertEquals(500, restApiRuntimeException.getStatus());
        assertEquals(Collections.emptyList(), restApiRuntimeException.getErrors());
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
    }

    @Test
    public void testCreateRestApiExceptionWebApplicationExceptionNullResponse() throws Exception
    {
        final WebApplicationException ex = new WebApplicationException((Response)null);
        final InvocationTargetException invocationTargetException = new InvocationTargetException(ex);

        final RestApiRuntimeException restApiRuntimeException = handler.createRestApiException(invocationTargetException);
        assertEquals(ex, restApiRuntimeException.getCause());
        assertEquals(500, restApiRuntimeException.getStatus());
        assertEquals(Collections.emptyList(), restApiRuntimeException.getErrors());
    }

    @Test
    public void testCreateRestApiExceptionWebApplicationException() throws Exception
    {
        final Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(FAILURE_STATUS);
        when(response.getStatusInfo()).thenReturn(mock(Response.StatusType.class));
        when(response.getStatusInfo().getStatusCode()).thenReturn(FAILURE_STATUS);

        final WebApplicationException ex = new WebApplicationException(response);
        final InvocationTargetException invocationTargetException = new InvocationTargetException(ex);

        final RestApiRuntimeException restApiRuntimeException = handler.createRestApiException(invocationTargetException);
        assertEquals(ex, restApiRuntimeException.getCause());
        assertEquals(FAILURE_STATUS, restApiRuntimeException.getStatus());
        assertEquals(Collections.emptyList(), restApiRuntimeException.getErrors());
    }
}