package com.smartling.api.external.client.exception;

import com.smartling.web.api.v2.Error;
import com.smartling.web.api.v2.ErrorResponse;
import com.smartling.web.api.v2.ResponseCode;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class RestApiRuntimeException extends WebApplicationException
{
    private final static Logger logger            = Logger.getLogger(RestApiRuntimeException.class.getName());
    private static final int    ERRORS_LOG_LIMIT  = 3;
    private static final String REQUEST_ID_HEADER = "X-SL-RequestId";

    private final ErrorResponse errorResponse;

    public RestApiRuntimeException(final Throwable cause)
    {
        super(cause);
        this.errorResponse = null;
    }

    public RestApiRuntimeException(final Throwable cause, final Response response)
    {
        super(cause, response);
        errorResponse = getErrorResponse(response);
    }

    private ErrorResponse getErrorResponse(final Response response)
    {
        if (response == null)
            return null;

        try
        {
            return response.readEntity(ErrorResponse.class);
        }
        catch (IllegalStateException ex)
        {
            // No body; no errors will be available
        }
        catch (ProcessingException ex)
        {
            logger.warning(String.format("Failed to process JSON: %s", ex.getMessage()));
        }

        return null;
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

        if (requestId != null)
            errorMessage.append(", requestId=").append(requestId);

        if (errorResponse != null)
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
