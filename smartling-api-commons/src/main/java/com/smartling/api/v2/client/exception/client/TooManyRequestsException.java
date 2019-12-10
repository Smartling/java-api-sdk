package com.smartling.api.v2.client.exception.client;

import com.smartling.api.v2.response.ErrorResponse;

import javax.ws.rs.core.Response;

/**
 * Thrown when the limit of simultaneous API requests per project or resource is exceeded.
 */
public class TooManyRequestsException extends ClientApiException
{
    public TooManyRequestsException(Throwable cause, Response response, ErrorResponse errorResponse)
    {
        super(cause, response, errorResponse);
    }
}
