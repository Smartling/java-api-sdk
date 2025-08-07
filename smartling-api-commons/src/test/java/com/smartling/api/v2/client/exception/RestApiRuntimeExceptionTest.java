package com.smartling.api.v2.client.exception;

import com.smartling.api.v2.client.request.RequestContext;
import com.smartling.api.v2.response.Error;
import com.smartling.api.v2.response.ErrorResponse;
import com.smartling.api.v2.response.ResponseCode;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RestApiRuntimeExceptionTest
{
    private static final int DEFAULT_STATUS        = 500;
    private static final int FAILURE_STATUS        = 400;
    private static final String REQUEST_ID = "test-request-id";
    private static final ResponseCode DEFAULT_CODE = ResponseCode.GENERAL_ERROR;

    @Mock
    private Response response;

    @Before
    public void setUp()
    {
        when(response.getStatus()).thenReturn(FAILURE_STATUS);
        when(response.getStatusInfo()).thenReturn(mock(Response.StatusType.class));
        when(response.getStatusInfo().getStatusCode()).thenReturn(FAILURE_STATUS);
        when(response.getHeaderString("X-SL-RequestId")).thenReturn(REQUEST_ID);
    }

    @Test
    public void testExceptionWithNoResponse()
    {
        final RestApiRuntimeException exception = new RestApiRuntimeException(new RuntimeException());
        assertEquals(DEFAULT_STATUS, exception.getStatus());
        assertEquals(DEFAULT_CODE, exception.getResponseCode());
        assertEquals(Collections.emptyList(), exception.getErrors());
        assertEquals("http_status=500", exception.getMessage());
    }

    @Test
    public void testExceptionWithRequestContext()
    {
        MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<>();
        headers.add("X-SL-RequestId", REQUEST_ID);

        RequestContext requestContext = new RequestContext(
            "POST",
            URI.create("https://api.smartling.com/test"),
            headers
        );

        final RestApiRuntimeException exception = new RestApiRuntimeException(new RuntimeException(), requestContext);
        assertEquals(DEFAULT_STATUS, exception.getStatus());
        assertEquals(DEFAULT_CODE, exception.getResponseCode());
        assertEquals(Collections.emptyList(), exception.getErrors());
        assertEquals("http_status=500, requestId=test-request-id", exception.getMessage());
    }

    @Test
    public void testExceptionWithRequestContextAndEmptyRequestIdHeader()
    {
        RequestContext requestContext = new RequestContext(
            "POST",
            URI.create("https://api.smartling.com/test"),
            new MultivaluedMapImpl<String, Object>() {{ putSingle("key", "value"); }}
        );

        final RestApiRuntimeException exception = new RestApiRuntimeException(new RuntimeException(), requestContext);
        assertEquals(DEFAULT_STATUS, exception.getStatus());
        assertEquals(DEFAULT_CODE, exception.getResponseCode());
        assertEquals(Collections.emptyList(), exception.getErrors());
        assertEquals("http_status=500", exception.getMessage());
    }

    @Test
    public void testExceptionWithNullRequestContext()
    {
        final RestApiRuntimeException exception = new RestApiRuntimeException(new RuntimeException(), null);
        assertEquals(DEFAULT_STATUS, exception.getStatus());
        assertEquals(DEFAULT_CODE, exception.getResponseCode());
        assertEquals(Collections.emptyList(), exception.getErrors());
        assertEquals("http_status=500", exception.getMessage());
    }

    @Test
    public void testExceptionWithResponseAndNullEntity()
    {
        final RestApiRuntimeException exception = new RestApiRuntimeException(new RuntimeException(), response, null);
        assertEquals(FAILURE_STATUS, exception.getStatus());
        assertEquals(DEFAULT_CODE, exception.getResponseCode());
        assertEquals(Collections.emptyList(), exception.getErrors());
        assertEquals("http_status=400, requestId=test-request-id", exception.getMessage());
    }

    @Test
    public void testExceptionWithResponse()
    {
        final List<Error> errors = new LinkedList<>();
        errors.add(new Error("foo"));
        final ErrorResponse errorResponse = new ErrorResponse(ResponseCode.VALIDATION_ERROR, errors);

        final RestApiRuntimeException exception = new RestApiRuntimeException(new RuntimeException(), response, errorResponse);
        assertEquals(FAILURE_STATUS, exception.getStatus());
        assertEquals(ResponseCode.VALIDATION_ERROR, exception.getResponseCode());
        assertEquals(errors, exception.getErrors());
        assertEquals("http_status=400, requestId=test-request-id, top errors: 'foo'", exception.getMessage());
    }

    @Test
    public void messageShouldIncludeTop3Errors()
    {
        final List<Error> errors = new LinkedList<>();
        errors.add(new Error("foo1"));
        errors.add(new Error("foo2"));
        errors.add(new Error("foo3"));
        errors.add(new Error("foo4"));
        final ErrorResponse errorResponse = new ErrorResponse(ResponseCode.VALIDATION_ERROR, errors);

        final RestApiRuntimeException exception = new RestApiRuntimeException(new RuntimeException(), response, errorResponse);

        String message = exception.getMessage();
        assertThat(message, containsString("http_status=400, requestId=test-request-id, top errors: "));
        assertThat(message, containsString("foo1"));
        assertThat(message, containsString("foo2"));
        assertThat(message, containsString("foo3"));
        assertThat(message, not(containsString("foo4")));
    }
}
