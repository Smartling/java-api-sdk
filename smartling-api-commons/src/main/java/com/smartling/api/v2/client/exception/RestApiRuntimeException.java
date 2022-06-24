package com.smartling.api.v2.client.exception;

import com.smartling.api.v2.response.Error;
import com.smartling.api.v2.response.ErrorResponse;
import com.smartling.api.v2.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Slf4j
public class RestApiRuntimeException extends WebApplicationException
{
    private static final int    ERRORS_LOG_LIMIT  = 3;
    private static final String REQUEST_ID_HEADER = "X-SL-RequestId";

    private final ErrorResponse errorResponse;
    private String errorDetails;

    public RestApiRuntimeException(final Throwable cause)
    {
        super(cause);
        this.errorResponse = null;
    }

    public RestApiRuntimeException(final Throwable cause, final Response response, final ErrorResponse errorResponse)
    {
        super(cause, response);
        this.errorResponse = errorResponse;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public int getStatus()
    {
        final Response response = getResponse();
        if (response == null)
            return 500;

        return response.getStatus();
    }

    public ResponseCode getResponseCode()
    {
        if (errorResponse == null || errorResponse.getCode() == null)
            return ResponseCode.GENERAL_ERROR;

        return errorResponse.getCode();

    }

    public List<Error> getErrors()
    {
        if (errorResponse == null || errorResponse.getErrors() == null)
            return Collections.emptyList();

        return errorResponse.getErrors();
    }

    @Override
    public String getMessage()
    {
        final String requestId = getResponse().getHeaderString(REQUEST_ID_HEADER);

        final StringBuilder errorMessage = new StringBuilder();

        errorMessage.append("http_status=").append(getStatus());

        if (errorDetails != null)
            errorMessage.append(errorDetails);

        if (requestId != null)
            errorMessage.append(", requestId=").append(requestId);

        if (errorResponse != null && errorResponse.getErrors() != null)
        {
            errorMessage.append(", top errors:");
            for (int i = 0; i < errorResponse.getErrors().size() && i < ERRORS_LOG_LIMIT; i++)
            {
                Error error = errorResponse.getErrors().get(i);

                errorMessage.append(" '").append(error.getMessage()).append("'");
            }
        }

        return errorMessage.toString();
    }
}
