package com.smartling.api.v2.client.exception.server;

import com.smartling.api.v2.response.ErrorResponse;

import javax.ws.rs.core.Response;

/**
 * Thrown to indicate that Smartling has entered maintenance mode,
 * all API services will return this error. After exiting maintenance mode,
 * all services should return to normal.
 */
public class MaintanenceModeErrorException extends ServerApiException
{
    public MaintanenceModeErrorException(Throwable cause, Response response, ErrorResponse errorResponse)
    {
        super(cause, response, errorResponse);
    }
}
