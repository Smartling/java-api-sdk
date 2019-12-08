package com.smartling.api.v2.client.exception.server;

import com.smartling.api.v2.client.exception.RestApiRuntimeException;
import com.smartling.api.v2.response.ErrorResponse;

import javax.ws.rs.core.Response;

/**
 * Thrown to indicate unexpected error occurred during
 * the service request processing.
 */
public class ServerApiException extends RestApiRuntimeException
{
    public ServerApiException(Throwable cause, Response response, ErrorResponse errorResponse)
    {
        super(cause, response, errorResponse);
    }
}
