package com.smartling.api.v2.client.exception.client;

import com.smartling.api.v2.response.ErrorResponse;

import javax.ws.rs.core.Response;

/**
 * Thrown when the create/modify request doesn't conform rules for the resource.
 * Usually it means that something specific was wrong with one or more request
 * parameters.
 */
public class ValidationErrorException extends ClientApiException
{
    public ValidationErrorException(Throwable cause, Response response, ErrorResponse errorResponse)
    {
        super(cause, response, errorResponse);
    }
}
