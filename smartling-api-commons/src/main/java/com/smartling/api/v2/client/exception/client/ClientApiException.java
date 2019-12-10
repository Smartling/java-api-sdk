package com.smartling.api.v2.client.exception.client;

import com.smartling.api.v2.client.exception.RestApiRuntimeException;
import com.smartling.api.v2.response.ErrorResponse;

import javax.ws.rs.core.Response;

/**
 * Thrown to indicate the generic error in the request parameters.
 */
public class ClientApiException extends RestApiRuntimeException
{
    public ClientApiException(Throwable cause, Response response, ErrorResponse errorResponse)
    {
        super(cause, response, errorResponse);
    }
}
