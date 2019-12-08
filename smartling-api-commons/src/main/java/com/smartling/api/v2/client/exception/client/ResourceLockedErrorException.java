package com.smartling.api.v2.client.exception.client;

import com.smartling.api.v2.response.ErrorResponse;

import javax.ws.rs.core.Response;

/**
 * Thrown to indicate that the resource can not be accessed right now.
 * Used sent by the Files API when when the modified file is already
 * being processed in the background.
 */
public class ResourceLockedErrorException extends ClientApiException
{
    public ResourceLockedErrorException(Throwable cause, Response response, ErrorResponse errorResponse)
    {
        super(cause, response, errorResponse);
    }
}
