package com.smartling.web.api.v2;

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
