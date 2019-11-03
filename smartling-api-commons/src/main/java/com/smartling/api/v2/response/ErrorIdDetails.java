package com.smartling.api.v2.response;

public class ErrorIdDetails extends Details
{
    private String errorId;

    public ErrorIdDetails()
    {
    }

    public ErrorIdDetails(String errorId)
    {
        this.errorId = errorId;
    }

    public String getErrorId()
    {
        return errorId;
    }

    public void setErrorId(String errorId)
    {
        this.errorId = errorId;
    }
}
