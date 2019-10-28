package com.smartling.web.api.v2;

/**
 * Successful response including the given data.
 *
 * @param <T> the type of data included in this response
 */
public class SuccessResponse<T extends ResponseData> extends Response<T>
{
    private T data;

    /**
     * Creates a new success response with the given data.
     *
     * @param data the response data of type <code>T</code>
     */
    public SuccessResponse(T data)
    {
        super(ResponseCode.SUCCESS);
        this.data = data;
    }

    /**
     * Creates a new success response with the given data.
     *
     * @param code the <code>ResponseCode</code> for this response
     * @param data the response data of type <code>T</code>
     */
    public SuccessResponse(ResponseCode code, T data)
    {
        super(code);
        this.data = data;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }
}
