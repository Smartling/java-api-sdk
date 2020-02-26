package com.smartling.api.jobbatches.exceptions;

import com.smartling.api.v2.client.exception.DefaultRestApiExceptionMapper;
import com.smartling.api.v2.client.exception.RestApiRuntimeException;
import com.smartling.api.v2.response.Error;
import com.smartling.api.v2.response.ErrorResponse;

import javax.ws.rs.core.Response;

import static com.smartling.api.v2.response.ResponseCode.GENERAL_ERROR;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;

public class JobBatchesApiExceptionMapper extends DefaultRestApiExceptionMapper
{
    private static final String KEY_PARSE_ERROR = "parse.error";
    private static final String MSG_NO_SOURCE_STRINGS_FOUND_ERROR = "No source strings found;";

    @Override
    public RestApiRuntimeException toException(Throwable throwable, Response response, ErrorResponse errorResponse)
    {
        if (errorResponse == null
            || errorResponse.getCode() == null
            || errorResponse.getCode() != GENERAL_ERROR
            || response.getStatus() != SC_BAD_REQUEST)
        {
            return super.toException(throwable, response, errorResponse);
        }

        for (Error error : errorResponse.getErrors())
        {
            if (error.getMessage() != null
                && error.getMessage().contains(KEY_PARSE_ERROR)
                && error.getMessage().contains(MSG_NO_SOURCE_STRINGS_FOUND_ERROR))
            {
                return new NoSourceStringsFoundException(throwable, response, errorResponse);
            }
        }

        return super.toException(throwable, response, errorResponse);
    }
}
