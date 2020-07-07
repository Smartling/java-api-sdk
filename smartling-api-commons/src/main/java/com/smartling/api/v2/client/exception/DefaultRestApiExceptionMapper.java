package com.smartling.api.v2.client.exception;

import com.smartling.api.v2.client.exception.client.AuthenticationErrorException;
import com.smartling.api.v2.client.exception.client.AuthorizationErrorException;
import com.smartling.api.v2.client.exception.client.ClientApiException;
import com.smartling.api.v2.client.exception.client.NotFoundErrorException;
import com.smartling.api.v2.client.exception.client.ResourceLockedErrorException;
import com.smartling.api.v2.client.exception.client.TooManyRequestsException;
import com.smartling.api.v2.client.exception.client.ValidationErrorException;
import com.smartling.api.v2.client.exception.server.MaintanenceModeErrorException;
import com.smartling.api.v2.client.exception.server.ServerApiException;
import com.smartling.api.v2.client.exception.server.ServiceBusyErrorException;
import com.smartling.api.v2.response.ErrorResponse;
import com.smartling.api.v2.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import javax.ws.rs.core.Response;

@Slf4j
public class DefaultRestApiExceptionMapper implements RestApiExceptionMapper
{
    @Override
    public RestApiRuntimeException toException(Throwable throwable, Response response, ErrorResponse errorResponse)
    {
        RestApiRuntimeException restApiRuntimeException;
        if (errorResponse != null && errorResponse.getCode() != null)
        {
            final ResponseCode code = errorResponse.getCode();

            // Smartling client response codes
            switch (code)
            {
                case AUTHORIZATION_ERROR:
                    restApiRuntimeException = new AuthorizationErrorException(throwable, response, errorResponse);
                    break;
                case AUTHENTICATION_ERROR:
                    restApiRuntimeException = new AuthenticationErrorException(throwable, response, errorResponse);
                    break;
                case NOT_FOUND_ERROR:
                    restApiRuntimeException = new NotFoundErrorException(throwable, response, errorResponse);
                    break;
                case VALIDATION_ERROR:
                    restApiRuntimeException = toExceptionByStatus(throwable, response, errorResponse);
                    break;
                case MAX_OPERATIONS_LIMIT_EXCEEDED:
                    restApiRuntimeException = new TooManyRequestsException(throwable, response, errorResponse);
                    break;
                case RESOURCE_LOCKED:
                    restApiRuntimeException = new ResourceLockedErrorException(throwable, response, errorResponse);
                    break;

                // Smartling server response codes
                case MAINTENANCE_MODE_ERROR:
                    restApiRuntimeException = new MaintanenceModeErrorException(throwable, response, errorResponse);
                    break;
                case SERVICE_BUSY:
                    restApiRuntimeException = new ServiceBusyErrorException(throwable, response, errorResponse);
                    break;
                case GENERAL_ERROR:
                    restApiRuntimeException = new ServerApiException(throwable, response, errorResponse);
                    break;
                default:
                    restApiRuntimeException = createGenericException(throwable, response, errorResponse);
                    break;
            }
        }

        else
        {
            restApiRuntimeException = createGenericException(throwable, response, errorResponse);
        }

        return restApiRuntimeException;
    }

    private RestApiRuntimeException toExceptionByStatus(Throwable throwable, Response response, ErrorResponse errorResponse)
    {
        RestApiRuntimeException restApiRuntimeException;
        switch (response.getStatus())
        {
            case HttpStatus.SC_UNAUTHORIZED:
                restApiRuntimeException = new AuthenticationErrorException(throwable, response, errorResponse);
                break;
            case HttpStatus.SC_NOT_FOUND:
                restApiRuntimeException = new NotFoundErrorException(throwable, response, errorResponse);
                break;
            default:
                restApiRuntimeException = new ValidationErrorException(throwable, response, errorResponse);
                break;
        }
        return restApiRuntimeException;
    }

    private RestApiRuntimeException createGenericException(Throwable throwable, Response response, ErrorResponse errorResponse)
    {
        RestApiRuntimeException restApiRuntimeException;

        final int statusCode = response.getStatus();

        // Generic statuses
        if (statusCode > 399 && statusCode < 500)
        {
            restApiRuntimeException = new ClientApiException(throwable, response, errorResponse);
        }
        else if (statusCode > 499 && statusCode < 600)
        {
            restApiRuntimeException = new ServerApiException(throwable, response, errorResponse);
        }

        // Unknown statuses
        else
        {
            restApiRuntimeException = new RestApiRuntimeException(throwable, response, errorResponse);
        }
        return restApiRuntimeException;
    }
}
