package com.smartling.api.v2.client.exception.server;

import com.smartling.api.v2.response.ErrorResponse;

import javax.ws.rs.core.Response;

/**
 * Thrown to indicate a temporary performance issue in the service.
 * Request might be repeated later.
 */
public class ServiceBusyErrorException extends ServerApiException
{
    public ServiceBusyErrorException(Throwable cause, Response response, ErrorResponse errorResponse)
    {
        super(cause, response, errorResponse);
    }
}
