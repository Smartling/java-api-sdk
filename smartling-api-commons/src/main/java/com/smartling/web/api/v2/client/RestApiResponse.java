package com.smartling.web.api.v2.client;

import com.smartling.sdk.v2.ResponseData;
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
