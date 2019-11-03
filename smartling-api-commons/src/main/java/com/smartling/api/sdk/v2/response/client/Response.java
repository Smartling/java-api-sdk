package com.smartling.api.sdk.v2.response.client;

import com.smartling.api.sdk.v2.response.Error;
import com.smartling.api.sdk.v2.response.ResponseCode;
import com.smartling.api.sdk.v2.response.ResponseData;
import java.util.List;

/**
 * Response class to be used on the client side for unmarshalling
 *
 * @param <T> the type of data included in this response
 */
public class Response<T extends ResponseData> extends com.smartling.api.sdk.v2.response.Response<T>
{
    private T            data;
    private List<Error>  errors;

    public Response()
    {
    }

    public Response(T data)
    {
        super(ResponseCode.SUCCESS);
        this.data = data;
    }

    public Response(ResponseCode code, List<Error> errors)
    {
        super(code);
        this.errors = errors;
    }

    public Response(ResponseCode code, T data, List<Error> errors)
    {
        super(code);
        this.data = data;
        this.errors = errors;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public List<Error> getErrors()
    {
        return errors;
    }

    public void setErrors(List<Error> errors)
    {
        this.errors = errors;
    }
}
