package com.smartling.web.api.v2.client;

import com.smartling.web.api.v2.Error;
import com.smartling.web.api.v2.ResponseCode;
import com.smartling.sdk.v2.ResponseData;
import java.util.List;

/**
 * Response class to be used on the client side for unmarshalling
 *
 * @param <T> the type of data included in this response
 */
public class Response<T extends ResponseData> extends com.smartling.web.api.v2.Response<T>
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
