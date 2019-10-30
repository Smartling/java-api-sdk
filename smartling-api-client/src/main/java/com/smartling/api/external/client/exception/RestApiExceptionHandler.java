package com.smartling.api.external.client.exception;

import java.lang.reflect.InvocationTargetException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class RestApiExceptionHandler
{
    public RestApiRuntimeException createRestApiException(final Exception exception)
    {
        final Throwable throwable = (exception instanceof InvocationTargetException) ? exception.getCause() : exception;

        final RestApiRuntimeException restApiRuntimeException;
        if (throwable instanceof WebApplicationException)
        {
            final Response response = ((WebApplicationException)throwable).getResponse();
            restApiRuntimeException = new RestApiRuntimeException(throwable, response);
        }
        else
            restApiRuntimeException = new RestApiRuntimeException(throwable);

        return restApiRuntimeException;
    }
}
