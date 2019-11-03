package com.smartling.api.sdk.v2.response;

/**
 * An API error to be returned to the caller.
 */
public class Error
{
    private String key;
    private String message;
    private Details details;

    /**
     * Creates a new, empty error.
     */
    protected Error()
    {
    }

    /**
     * Creates a new error from the given exception.
     *
     * @param ex the exception causing this error to be returned (required)
     */
    public Error(Exception ex)
    {
        this(ex.getMessage());
    }

    /**
     * Creates a new error with the given message.
     *
     * @param message the reason this error is being returned (required)
     */
    public Error(String message)
    {
        this(null, message, null);
    }

    /**
     * Creates a new error with the given key and message
     *
     * @param key the key for this error (optional)
     * @param message the reason this error is being returned (required)
     */
    public Error(String key, String message)
    {
        this(key, message, null);
    }

    /**
     * Creates a new error with the given key, message, and details.
     *
     * @param key the key for this error (optional)
     * @param message the reason this error is being returned (required)
     * @param details an object containing details about this error (optional)
     */
    public Error(String key, String message, Details details)
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

    public Details getDetails()
    {
        return details;
    }

    public void setDetails(Details details)
    {
        this.details = details;
    }
}
