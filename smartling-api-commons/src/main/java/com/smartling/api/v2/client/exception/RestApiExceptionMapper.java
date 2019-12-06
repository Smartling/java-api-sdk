package com.smartling.api.v2.client.exception;

import com.smartling.api.v2.response.ErrorResponse;

import javax.ws.rs.core.Response;

public interface RestApiExceptionMapper
{
    RestApiRuntimeException toException(Throwable throwable, Response response, ErrorResponse errorResponse);
}
