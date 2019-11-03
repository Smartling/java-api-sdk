package com.smartling.api.v2.response;

import java.util.Arrays;
import java.util.List;

/**
 * API response builders to simplify creating responses from controllers.
 *
 * @author Scott Rossillo
 */
public class ResponseBuilders
{
    /**
     * Creates a successful response with the given data.
     *
     * @param data the response data transfer object (required)
     * @param <T> the type of <code>data</code> being returned
     * @return an <code>Response</code> containing the given <code>data</code>
     */
    public static <T extends ResponseData> RestApiResponse<T> respondWith(T data)
    {
        return wrap(new SuccessResponse<>(data));
    }

    /**
     * Creates a successful response with the given list data.
     *
     * @param data the response data transfer object (required)
     * @param <T> the type of <code>data</code> being returned
     * @return an <code>Response</code> containing the given <code>data</code>
     */
    public static <T extends ResponseData> RestApiResponse<ListResponse<T>> respondWith(List<T> data)
    {
        return wrap(new SuccessResponse<>(new ListResponse<>(data)));
    }

    /**
     * Creates an error response with the given response code and errors.
     *
     * @param code the <code>ResponseCode</code> to send with the response (required)
     * @param errors the errors to send with the response (required)
     * @return an <code>ErrorResponse</code> containing the given <code>code</code> and <code>messages</code>
     */
    public static RestApiResponse<EmptyData> respondWith(ResponseCode code, Error... errors)
    {
        return respondWith(code, Arrays.asList(errors));
    }

    /**
     * Creates an error response with the given response code and messages.
     *
     * @param code the <code>ResponseCode</code> to send with the response (required)
     * @param errors the errors to send with the response (required)
     * @return an <code>ApiResponse</code> containing the given <code>code</code> and <code>messages</code>
     */
    public static RestApiResponse<EmptyData> respondWith(ResponseCode code, List<Error> errors)
    {
        return wrap(new ErrorResponse(code, errors));
    }

    /**
     * Creates an error response with the given response code and exception.
     *
     * @param code the <code>ResponseCode</code> to send with the response (required)
     * @param ex the <code>Exception</code> to send with the response (required)
     * @return an <code>ApiResponse</code> containing the given <code>code</code> and exception
     */
    public static RestApiResponse<EmptyData> respondWith(ResponseCode code, Exception ex)
    {
        return respondWith(code, new Error(ex));
    }

    // Wraps a response in a RestApiResponse.
    private static <T extends ResponseData> RestApiResponse<T> wrap(Response<T> response)
    {
        return new RestApiResponse<>(response);
    }
}
