package com.smartling.api.v2.client.exception;

import com.smartling.api.v2.client.exception.client.ClientApiException;
import com.smartling.api.v2.client.exception.client.NotFoundErrorException;
import com.smartling.api.v2.client.exception.client.ValidationErrorException;
import com.smartling.api.v2.client.exception.server.ServerApiException;
import com.smartling.api.v2.response.ErrorResponse;
import com.smartling.api.v2.response.ResponseCode;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultRestApiExceptionMapperTest
{
    private RestApiExceptionMapper exceptionMapper;

    @Mock
    private ErrorResponse errorResponse;
    @Mock
    private Throwable throwable;
    @Mock
    private Response response;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        exceptionMapper = new DefaultRestApiExceptionMapper();
        when(response.getStatusInfo()).thenReturn(mock(Response.StatusType.class));
    }

    @Test
    public void testCreateSmartlingExceptionByResponseCode() throws Exception
    {
        when(errorResponse.getCode()).thenReturn(ResponseCode.VALIDATION_ERROR);

        RestApiRuntimeException ex = exceptionMapper.toException(throwable, response, errorResponse);
        assertTrue(ex instanceof ValidationErrorException);
    }

    @Test
    public void testCreateGenericClientException() throws Exception
    {
        when(response.getStatus()).thenReturn(480);

        RestApiRuntimeException ex = exceptionMapper.toException(throwable, response, errorResponse);
        assertTrue(ex instanceof ClientApiException);
    }

    @Test
    public void testCreateAuthenticationErrorException() throws Exception
    {
        when(response.getStatus()).thenReturn(HttpStatus.SC_UNAUTHORIZED);
        when(errorResponse.getCode()).thenReturn(ResponseCode.VALIDATION_ERROR);

        RestApiRuntimeException ex = exceptionMapper.toException(throwable, response, errorResponse);
        assertTrue(ex instanceof ClientApiException);
    }

    @Test
    public void testCreateGenericServerException() throws Exception
    {
        when(response.getStatus()).thenReturn(584);

        RestApiRuntimeException ex = exceptionMapper.toException(throwable, response, errorResponse);
        assertTrue(ex instanceof ServerApiException);
    }

    @Test
    public void testCreateGenericRestApiException() throws Exception
    {
        when(response.getStatus()).thenReturn(640);

        RestApiRuntimeException ex = exceptionMapper.toException(throwable, response, errorResponse);
        assertEquals(ex.getClass(), RestApiRuntimeException.class);
    }

    @Test
    public void testCreateNotFoundByResponseCode()
    {
        when(response.getStatus()).thenReturn(HttpStatus.SC_NOT_FOUND);
        when(errorResponse.getCode()).thenReturn(ResponseCode.VALIDATION_ERROR);

        RestApiRuntimeException ex = exceptionMapper.toException(throwable, response, errorResponse);
        assertTrue(ex instanceof NotFoundErrorException);
    }
}
