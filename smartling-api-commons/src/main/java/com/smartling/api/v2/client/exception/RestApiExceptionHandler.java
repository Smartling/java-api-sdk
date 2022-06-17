package com.smartling.api.v2.client.exception;

import com.smartling.api.v2.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class RestApiExceptionHandler
{
    private final RestApiExceptionMapper exceptionMapper;

    public RestApiExceptionHandler(RestApiExceptionMapper exceptionMapper)
    {
        this.exceptionMapper = exceptionMapper;
    }

    public RestApiRuntimeException createRestApiException(final Exception exception)
    {
        return createRestApiException(exception, null);
    }

    public RestApiRuntimeException createRestApiException(final Exception exception, String errorDetails)
    {
        final Throwable throwable = (exception instanceof InvocationTargetException) ? exception.getCause() : exception;

        final RestApiRuntimeException restApiRuntimeException;
        if (throwable instanceof WebApplicationException)
        {
            final Response response = ((WebApplicationException)throwable).getResponse();
            final ErrorResponse errorResponse = getErrorResponse(response);

            restApiRuntimeException = exceptionMapper.toException(throwable, response, errorResponse);
        }
        else if (throwable instanceof ProcessingException && throwable.getCause() instanceof RestApiRuntimeException)
        {
            restApiRuntimeException = (RestApiRuntimeException)throwable.getCause();
        }
        else
        {
            restApiRuntimeException = new RestApiRuntimeException(throwable);
        }

        restApiRuntimeException.setErrorDetails(errorDetails);
        return restApiRuntimeException;
    }

    private ErrorResponse getErrorResponse(final Response response)
    {
        if (response == null)
        {
            return null;
        }

        try
        {
            return response.readEntity(ErrorResponse.class);
        }
        catch (IllegalStateException ex)
        {
            // No body; no errors will be available
        }
        catch (ProcessingException ex)
        {
            log.warn("Failed to process JSON: {}", ex.getMessage());
        }

        return null;
    }
}
