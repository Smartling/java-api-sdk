package com.smartling.api.v2.response;

public enum ErrorKey
{
    RESOURCE_NOT_FOUND("Resource was not found."),
    RESOURCE_NAME_IS_NOT_UNIQUE("Resource name is not unique.");

    private final String message;

    ErrorKey(String message)
    {
        this.message = message;
    }

    /**
     * Key is a lowercased string to comply rules&best practises for API.
     *
     * @return name() in lower case
     */
    public String getKey()
    {
        return this.name().toLowerCase();
    }

    public String getMessage()
    {
        return message;
    }
}
