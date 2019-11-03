package com.smartling.api.sdk.v2.response;

import java.io.Serializable;

public class RestApiResponse<T extends ResponseData> implements Serializable
{
    private Response<T> response;

    public RestApiResponse(Response<T> response)
    {
        this.response = response;
    }

    public Response<T> getResponse()
    {
        return response;
    }
}
