package com.smartling.api.filetranslations.v2.pto;

public class Error<T>
{
    private String key;
    private String message;
    private T details;

    public Error()
    {
    }

    public Error(String key, String message, T details)
    {
        this.key = key;
        this.message = message;
        this.details = details;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public T getDetails()
    {
        return details;
    }

    public void setDetails(T details)
    {
        this.details = details;
    }

    @Override
    public String toString()
    {
        return "Error{" +
                "key='" + key + '\'' +
                ", message='" + message + '\'' +
                ", details=" + details +
                '}';
    }
}
