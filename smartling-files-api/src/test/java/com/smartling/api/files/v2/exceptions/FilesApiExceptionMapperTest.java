package com.smartling.api.files.v2.exceptions;

import com.smartling.api.v2.client.exception.RestApiExceptionMapper;
import com.smartling.api.v2.client.exception.RestApiRuntimeException;
import com.smartling.api.v2.response.Error;
import com.smartling.api.v2.response.ErrorResponse;
import com.smartling.api.v2.response.ResponseCode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;

import java.util.Collections;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FilesApiExceptionMapperTest
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
        when(response.getStatusInfo()).thenReturn(mock(Response.StatusType.class));
        when(errorResponse.getCode()).thenReturn(ResponseCode.VALIDATION_ERROR);
        exceptionMapper = new FilesApiExceptionMapper();
    }

    @Test
    public void testCreateFileNotFoundException() throws Exception
    {
        Error error = new Error("file.not.found", "File not found");
        when(errorResponse.getErrors()).thenReturn(Collections.singletonList(error));

        RestApiRuntimeException ex = exceptionMapper.toException(throwable, response, errorResponse);
        assertTrue(ex instanceof FileNotFoundException);
    }

    @Test
    public void testCreateNoSourceStringsFoundException() throws Exception
    {
        Error error = new Error("parse.error", "There was a problem loading your file. No source strings found; Check your configuration http://goo.gl/1Tuqbj");
        when(errorResponse.getErrors()).thenReturn(Collections.singletonList(error));

        RestApiRuntimeException ex = exceptionMapper.toException(throwable, response, errorResponse);
        assertTrue(ex instanceof NoSourceStringsFoundException);
    }
}
