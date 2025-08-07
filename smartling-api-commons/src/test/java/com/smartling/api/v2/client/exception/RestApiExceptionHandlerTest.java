package com.smartling.api.v2.client.exception;

import com.smartling.api.v2.client.request.RequestContext;
import com.smartling.api.v2.client.request.RequestContextHolder;
import com.smartling.api.v2.response.ErrorResponse;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
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

    @InjectMocks
    private RestApiExceptionHandler handler;

    @Before
    public void setUp() throws Exception
    {
        when(exceptionMapper.toException(any(), any(), any())).thenReturn(restApiRuntimeException);
        when(restApiRuntimeException.getStatus()).thenReturn(FAILURE_STATUS);
        when(restApiRuntimeException.getCause()).thenReturn(cause);
        when(response.getStatusInfo()).thenReturn(mock(Response.StatusType.class));
    }

    @Test
    public void testCreateRestApiExceptionRuntimeException()
    {
        final RuntimeException ex = new RuntimeException();

        final RestApiRuntimeException restApiRuntimeException = handler.createRestApiException(ex);
        assertEquals(ex, restApiRuntimeException.getCause());
        assertEquals(500, restApiRuntimeException.getStatus());
        assertEquals("http_status=500", restApiRuntimeException.getMessage());
        assertEquals(Collections.emptyList(), restApiRuntimeException.getErrors());
        verify(exceptionMapper, never()).toException(any(Throwable.class), any(Response.class), any(ErrorResponse.class));
    }

    @Test
    public void testCreateRestApiExceptionRuntimeExceptionWhenRequestContextAvailable()
    {
        MultivaluedMap<String, Object> requestHeaders = new MultivaluedMapImpl<>();
        requestHeaders.putSingle("X-SL-RequestId", "test-request-id");

        RequestContext requestContext = mock(RequestContext.class);
        when(requestContext.getHeaders()).thenReturn(requestHeaders);
        RequestContextHolder.setContext(requestContext);

        final RuntimeException ex = new RuntimeException();

        final RestApiRuntimeException restApiRuntimeException = handler.createRestApiException(ex);
        assertEquals(ex, restApiRuntimeException.getCause());
        assertEquals(500, restApiRuntimeException.getStatus());
        assertEquals("http_status=500, requestId=test-request-id", restApiRuntimeException.getMessage());
        assertEquals(Collections.emptyList(), restApiRuntimeException.getErrors());
        verify(exceptionMapper, never()).toException(any(Throwable.class), any(Response.class), any(ErrorResponse.class));

        RequestContextHolder.clearContext();
    }

    @Test
    public void testCreateRestApiExceptionInvocationTargetException()
    {
        final RuntimeException ex = new RuntimeException();
        final InvocationTargetException invocationTargetException = new InvocationTargetException(ex);

        final RestApiRuntimeException restApiRuntimeException = handler.createRestApiException(invocationTargetException, ", details");
        assertEquals(ex, restApiRuntimeException.getCause());
        assertEquals(500, restApiRuntimeException.getStatus());
        assertEquals("http_status=500, details", restApiRuntimeException.getMessage());
        assertEquals(Collections.emptyList(), restApiRuntimeException.getErrors());
        verify(exceptionMapper, never()).toException(any(Throwable.class), any(Response.class), any(ErrorResponse.class));
    }

    @Test
    public void testCreateRestApiExceptionWebApplicationExceptionNullResponse()
    {
        final WebApplicationException ex = new WebApplicationException((Response)null);
        final InvocationTargetException invocationTargetException = new InvocationTargetException(ex);

        final RestApiRuntimeException restApiRuntimeException = handler.createRestApiException(invocationTargetException);
        assertEquals(cause, restApiRuntimeException.getCause());
        assertEquals(500, restApiRuntimeException.getStatus());
        assertEquals(Collections.emptyList(), restApiRuntimeException.getErrors());
        verify(exceptionMapper, times(1)).toException(eq(ex), any(Response.class), isNull());
    }

    @Test
    public void testCreateRestApiExceptionWebApplicationException()
    {
        final WebApplicationException ex = new WebApplicationException(response);
        final InvocationTargetException invocationTargetException = new InvocationTargetException(ex);

        final RestApiRuntimeException restApiRuntimeException = handler.createRestApiException(invocationTargetException);
        assertEquals(cause, restApiRuntimeException.getCause());
        assertEquals(FAILURE_STATUS, restApiRuntimeException.getStatus());
        assertEquals(Collections.emptyList(), restApiRuntimeException.getErrors());
        verify(exceptionMapper, times(1)).toException(eq(ex), eq(response), isNull());
    }

    @Test
    public void testCreateRestApiExceptionWebApplicationExceptionWithErrorsEntity()
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
    public void testCreateRestApiExceptionWebApplicationExceptionIllegalStateException()
    {
        final ErrorResponse errorResponse = mock(ErrorResponse.class);
        when(response.readEntity(ErrorResponse.class)).thenThrow(new IllegalStateException());

        final WebApplicationException ex = new WebApplicationException(response);
        final InvocationTargetException invocationTargetException = new InvocationTargetException(ex);

        final RestApiRuntimeException restApiRuntimeException = handler.createRestApiException(invocationTargetException);
        verify(exceptionMapper, times(1)).toException(eq(ex), eq(response), isNull());
    }

    @Test
    public void testCreateRestApiExceptionWebApplicationExceptionProcessingException()
    {
        when(response.readEntity(ErrorResponse.class)).thenThrow(new ProcessingException("foo"));

        final WebApplicationException ex = new WebApplicationException(response);
        final InvocationTargetException invocationTargetException = new InvocationTargetException(ex);

        handler.createRestApiException(invocationTargetException);

        verify(exceptionMapper, times(1)).toException(eq(ex), eq(response), isNull());
    }

    @Test
    public void shouldUnwrapProcessingException()
    {
        RestApiRuntimeException original = new RestApiRuntimeException(new Exception());
        ProcessingException processingException = new ProcessingException(original);
        final InvocationTargetException invocationTargetException = new InvocationTargetException(processingException);

        final RestApiRuntimeException restApiRuntimeException = handler.createRestApiException(invocationTargetException);
        assertEquals(restApiRuntimeException, original);
    }

    @Test
    public void shouldntUnwrapProcessingExceptionWithNullCause()
    {
        ProcessingException processingException = new ProcessingException("No Cause");
        final InvocationTargetException invocationTargetException = new InvocationTargetException(processingException);

        final RestApiRuntimeException restApiRuntimeException = handler.createRestApiException(invocationTargetException);
        assertEquals(restApiRuntimeException.getCause(), processingException);
    }

    @Test
    public void testCreateRestApiExceptionOnResponseProcessingException()
    {
        ResponseProcessingException ex = new ResponseProcessingException(response, "error");

        handler.createRestApiException(ex);

        verify(exceptionMapper, never()).toException(eq(ex), eq(response), isNull());
    }
}
