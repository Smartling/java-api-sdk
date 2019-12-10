package com.smartling.api.v2.client.exception.client;

import com.smartling.api.v2.response.ErrorResponse;

import javax.ws.rs.core.Response;

/**
 * Thrown to indicate that the requested resource not found.
 */
public class NotFoundErrorException extends ClientApiException
{
    public NotFoundErrorException(Throwable cause, Response response, ErrorResponse errorResponse)
    {
        super(cause, response, errorResponse);
    }
}
