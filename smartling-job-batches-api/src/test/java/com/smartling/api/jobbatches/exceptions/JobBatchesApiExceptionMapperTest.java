package com.smartling.api.jobbatches.exceptions;

import com.smartling.api.v2.client.exception.RestApiExceptionMapper;
import com.smartling.api.v2.client.exception.RestApiRuntimeException;
import com.smartling.api.v2.response.Error;
import com.smartling.api.v2.response.ErrorResponse;
import com.smartling.api.v2.response.ResponseCode;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JobBatchesApiExceptionMapperTest
{
    private RestApiExceptionMapper exceptionMapper;
    private Response response;

    @Before
    public void init()
    {
        response = mock(Response.class);
        when(response.getStatus()).thenReturn(HttpStatus.SC_BAD_REQUEST);
        when(response.getStatusInfo()).thenReturn(Response.Status.BAD_REQUEST);
        exceptionMapper = new JobBatchesApiExceptionMapper();
    }

    @Test
    public void shouldCreateNoSourceStringFoundException()
    {
        Exception throwable = new Exception();
        ErrorResponse errorResponse = new ErrorResponse(ResponseCode.GENERAL_ERROR, new Error("parse.error", "parse.error" + "No source strings found;"));

        RestApiRuntimeException exception = exceptionMapper.toException(throwable, response, errorResponse);

        assertTrue(exception instanceof NoSourceStringsFoundException);
    }

    @Test
    public void shouldPassNotGeneralErrorException()
    {
        Exception throwable = new Exception();
        ErrorResponse errorResponse = new ErrorResponse(ResponseCode.AUTHENTICATION_ERROR, new Error("parse.error", "parse.error" + "No source strings found;"));

        RestApiRuntimeException exception = exceptionMapper.toException(throwable, response, errorResponse);

        assertFalse(exception instanceof NoSourceStringsFoundException);
    }

    @Test
    public void shouldPassNotParseError()
    {
        Exception throwable = new Exception();
        ErrorResponse errorResponse = new ErrorResponse(ResponseCode.GENERAL_ERROR, new Error("any.error", "any.error" + "No source strings found;"));

        RestApiRuntimeException exception = exceptionMapper.toException(throwable, response, errorResponse);

        assertFalse(exception instanceof NoSourceStringsFoundException);
    }

    @Test
    public void shouldPassOtherParseErrors()
    {
        Exception throwable = new Exception();
        ErrorResponse errorResponse = new ErrorResponse(ResponseCode.GENERAL_ERROR, new Error("parse.error", "parse.error" + "Some error description;"));

        RestApiRuntimeException exception = exceptionMapper.toException(throwable, response, errorResponse);

        assertFalse(exception instanceof NoSourceStringsFoundException);
    }

    @Test
    public void shouldPassAllExceptBadRequestResponse()
    {
        when(response.getStatus()).thenReturn(HttpStatus.SC_NOT_FOUND);
        when(response.getStatusInfo()).thenReturn(Response.Status.NOT_FOUND);
        Exception throwable = new Exception();
        ErrorResponse errorResponse = new ErrorResponse(ResponseCode.GENERAL_ERROR, new Error("parse.error", "parse.error" + "Some error description;"));

        RestApiRuntimeException exception = exceptionMapper.toException(throwable, response, errorResponse);

        assertFalse(exception instanceof NoSourceStringsFoundException);
    }
}
