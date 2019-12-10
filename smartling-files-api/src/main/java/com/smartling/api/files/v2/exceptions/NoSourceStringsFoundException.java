package com.smartling.api.files.v2.exceptions;

import com.smartling.api.v2.client.exception.client.ValidationErrorException;
import com.smartling.api.v2.response.ErrorResponse;

import javax.ws.rs.core.Response;

/**
 * Thrown to indicate that no translatable strings werefound in the uploaded file.
 * It might also mean that translatable text exists but is not covered by directives.
 */
public class NoSourceStringsFoundException extends ValidationErrorException
{
    public NoSourceStringsFoundException(Throwable cause, Response response, ErrorResponse errorResponse)
    {
        super(cause, response, errorResponse);
    }
}
