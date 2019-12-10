package com.smartling.api.files.v2.exceptions;

import com.smartling.api.v2.client.exception.DefaultRestApiExceptionMapper;
import com.smartling.api.v2.client.exception.RestApiRuntimeException;
import com.smartling.api.v2.response.Error;
import com.smartling.api.v2.response.ErrorResponse;
import com.smartling.api.v2.response.ResponseCode;

import javax.ws.rs.core.Response;

import java.util.Objects;

import static com.smartling.api.v2.response.ResponseCode.VALIDATION_ERROR;

public class FilesApiExceptionMapper extends DefaultRestApiExceptionMapper
{
    private static final String KEY_FILE_NOT_FOUND = "file.not.found";
    private static final String KEY_PARSE_ERROR = "parse.error";
    private static final String MSG_NO_SOURCE_STRINGS_FOUND_ERROR = "No source strings found;";

    @Override
    public RestApiRuntimeException toException(Throwable throwable, Response response, ErrorResponse errorResponse)
    {
        if (errorResponse == null
            || errorResponse.getCode() == null
            || errorResponse.getCode() != VALIDATION_ERROR)
        {
            return super.toException(throwable, response, errorResponse);
        }

        for (Error error : errorResponse.getErrors())
        {
            if (Objects.equals(error.getKey(), KEY_FILE_NOT_FOUND))
            {
                return new FileNotFoundException(throwable, response, errorResponse);
            }

            if (Objects.equals(error.getKey(), KEY_PARSE_ERROR)
                && error.getMessage() != null
                && error.getMessage().contains(MSG_NO_SOURCE_STRINGS_FOUND_ERROR))
            {
                return new NoSourceStringsFoundException(throwable, response, errorResponse);
            }
        }

        return super.toException(throwable, response, errorResponse);
    }
}
