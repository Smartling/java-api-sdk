package com.smartling.api.v2.response.client;

import com.smartling.api.v2.response.ResponseData;

import java.io.Serializable;

/**
 * Response wrapper class to be used on the client side for unmarshalling
 *
 * @param <T> the type of data included in this response
 */
public class RestApiResponse<T extends ResponseData> implements Serializable
{
    private Response<T> response;

    public RestApiResponse()
    {
    }

    public RestApiResponse(Response<T> response)
    {
        this.response = response;
    }

    public Response<T> getResponse()
    {
        return response;
    }

    public void setResponse(Response<T> response)
    {
        this.response = response;
    }
}
