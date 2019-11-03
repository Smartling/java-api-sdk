package com.smartling.api.v2.response;

import java.io.Serializable;

/**
 * HTTP client response.
 *
 * @author Scott Rossillo
 */
public class ResponseEntity<T> implements Serializable
{
    private final int status;
    private final String message;
    private final T body;

    public ResponseEntity(int status, String message, T body)
    {
        this.status = status;
        this.message = message;
        this.body = body;
    }

    public int getStatus()
    {
        return status;
    }

    public String getMessage()
    {
        return message;
    }

    public T getBody()
    {
        return body;
    }
}
