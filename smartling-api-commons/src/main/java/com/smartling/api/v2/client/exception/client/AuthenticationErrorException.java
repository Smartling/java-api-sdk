package com.smartling.api.v2.client.exception.client;

import com.smartling.api.v2.response.ErrorResponse;

import javax.ws.rs.core.Response;

/**
 * Thwown to indacate that the provided credentials were not valid.
 */
public class AuthenticationErrorException extends ClientApiException
{
    public AuthenticationErrorException(Throwable cause, Response response, ErrorResponse errorResponse)
    {
        super(cause, response, errorResponse);
    }
}
