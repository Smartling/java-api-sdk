package com.smartling.api.sdk.v2.response;

/**
 * Abstract API response.
 */
public abstract class Response<T>
{
    private ResponseCode code;

    /**
     * Creates a new, empty response.
     */
    protected Response()
    {
    }

    /**
     * Creates a new response with the given code.
     *
     * @param code the <code>ResponseCode</code> for this response
     */
    protected Response(ResponseCode code)
    {
        this.code = code;
    }

    public ResponseCode getCode()
    {
        return code;
    }

    public void setCode(ResponseCode code)
    {
        this.code = code;
    }

}
