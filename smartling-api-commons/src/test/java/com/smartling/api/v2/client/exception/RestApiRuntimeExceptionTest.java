package com.smartling.api.v2.client.exception;

import com.smartling.api.v2.response.Error;
import com.smartling.api.v2.response.ErrorResponse;
import com.smartling.api.v2.response.ResponseCode;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
        final RestApiRuntimeException exception = new RestApiRuntimeException(new RuntimeException(), response, null);
        assertEquals(FAILURE_STATUS, exception.getStatus());
        assertEquals(DEFAULT_CODE, exception.getResponseCode());
        assertEquals(Collections.emptyList(), exception.getErrors());
    }

    @Test
    public void testExceptionWithResponse() throws Exception
    {
        final List<Error> errors = new LinkedList<>();
        errors.add(new Error("foo"));
        final ErrorResponse errorResponse = new ErrorResponse(ResponseCode.VALIDATION_ERROR, errors);

        final RestApiRuntimeException exception = new RestApiRuntimeException(new RuntimeException(), response, errorResponse);
        assertEquals(FAILURE_STATUS, exception.getStatus());
        assertEquals(ResponseCode.VALIDATION_ERROR, exception.getResponseCode());
        assertEquals(errors, exception.getErrors());
    }

    @Test
    public void customErrorMessage() throws Exception
    {
        final List<Error> errors = new LinkedList<>();
        errors.add(new Error("foo1"));
        errors.add(new Error("foo2"));
        errors.add(new Error("foo3"));
        errors.add(new Error("foo4"));
        final ErrorResponse errorResponse = new ErrorResponse(ResponseCode.VALIDATION_ERROR, errors);

        when(response.getHeaderString(anyString())).thenReturn("some request id");

        final RestApiRuntimeException exception = new RestApiRuntimeException(new RuntimeException(), response, errorResponse);

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
