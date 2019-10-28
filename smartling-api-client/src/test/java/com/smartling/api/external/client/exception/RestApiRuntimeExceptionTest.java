package com.smartling.api.external.client.exception;

import com.smartling.web.api.v2.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RestApiRuntimeExceptionTest
{
    private static final int DEFAULT_STATUS        = 500;
    private static final int FAILURE_STATUS        = 400;
    private static final ResponseCode DEFAULT_CODE = ResponseCode.GENERAL_ERROR;

    @Mock
    private Response response;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        when(response.getStatus()).thenReturn(FAILURE_STATUS);
        when(response.getStatusInfo()).thenReturn(mock(Response.StatusType.class));
        when(response.getStatusInfo().getStatusCode()).thenReturn(FAILURE_STATUS);
    }

    @Test
    public void testExceptionWithNoResponse() throws Exception
    {
        final RestApiRuntimeException exception = new RestApiRuntimeException(new RuntimeException());
        assertEquals(DEFAULT_STATUS, exception.getStatus());
        assertEquals(DEFAULT_CODE, exception.getResponseCode());
        assertEquals(Collections.emptyList(), exception.getErrors());
    }

    @Test
    public void testExceptionWithResponseNullEntity() throws Exception
    {
        final RestApiRuntimeException exception = new RestApiRuntimeException(new RuntimeException(), response);
        assertEquals(FAILURE_STATUS, exception.getStatus());
        assertEquals(DEFAULT_CODE, exception.getResponseCode());
        assertEquals(Collections.emptyList(), exception.getErrors());
        verify(response, times(1)).readEntity(ErrorResponse.class);
    }

    @Test
    public void testExceptionWithResponseIllegalStateException() throws Exception
    {
        when(response.readEntity(ErrorResponse.class)).thenThrow(new IllegalStateException());
        final RestApiRuntimeException exception = new RestApiRuntimeException(new RuntimeException(), response);
        assertEquals(FAILURE_STATUS, exception.getStatus());
        assertEquals(DEFAULT_CODE, exception.getResponseCode());
        assertEquals(Collections.emptyList(), exception.getErrors());
        verify(response, times(1)).readEntity(ErrorResponse.class);
    }

    @Test
    public void testExceptionWithResponseProcessingException() throws Exception
    {
        when(response.readEntity(ErrorResponse.class)).thenThrow(new ProcessingException("foo"));
        final RestApiRuntimeException exception = new RestApiRuntimeException(new RuntimeException(), response);
        assertEquals(FAILURE_STATUS, exception.getStatus());
        assertEquals(DEFAULT_CODE, exception.getResponseCode());
        assertEquals(Collections.emptyList(), exception.getErrors());
        verify(response, times(1)).readEntity(ErrorResponse.class);
    }

    @Test
    public void testExceptionWithResponse() throws Exception
    {
        final List<com.smartling.web.api.v2.Error> errors = new LinkedList<>();
        errors.add(new com.smartling.web.api.v2.Error("foo"));

        when(response.readEntity(ErrorResponse.class)).thenReturn(new ErrorResponse(ResponseCode.VALIDATION_ERROR, errors));
        final RestApiRuntimeException exception = new RestApiRuntimeException(new RuntimeException(), response);
        assertEquals(FAILURE_STATUS, exception.getStatus());
        assertEquals(ResponseCode.VALIDATION_ERROR, exception.getResponseCode());
        assertEquals(errors, exception.getErrors());
        verify(response, times(1)).readEntity(ErrorResponse.class);
    }

    @Test
    public void customErrorMessage() throws Exception
    {
        final List<com.smartling.web.api.v2.Error> errors = new LinkedList<>();
        errors.add(new com.smartling.web.api.v2.Error("foo1"));
        errors.add(new com.smartling.web.api.v2.Error("foo2"));
        errors.add(new com.smartling.web.api.v2.Error("foo3"));
        errors.add(new com.smartling.web.api.v2.Error("foo4"));

        when(response.readEntity(ErrorResponse.class)).thenReturn(new ErrorResponse(ResponseCode.VALIDATION_ERROR, errors));
        when(response.getHeaderString(anyString())).thenReturn("some request id");

        final RestApiRuntimeException exception = new RestApiRuntimeException(new RuntimeException(), response);

        String message = exception.getMessage();
        assertThat(message, containsString("foo1"));
        assertThat(message, containsString("foo2"));
        assertThat(message, containsString("foo3"));
        assertThat(message, not(containsString("foo4")));
        assertThat(message, containsString("some request id"));
        assertThat(message, containsString("400"));

        verify(response).getHeaderString("X-SL-RequestId");
    }
}