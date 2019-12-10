package com.smartling.api.files.v2.exceptions;

import com.smartling.api.v2.client.exception.client.ValidationErrorException;
import com.smartling.api.v2.response.ErrorResponse;

import javax.ws.rs.core.Response;

/**
 * Thrown to indicate that the requested file not found
 */
public class FileNotFoundException extends ValidationErrorException
{
    public FileNotFoundException(Throwable cause, Response response, ErrorResponse errorResponse)
    {
        super(cause, response, errorResponse);
    }
}
