package com.smartling.web.api.v2;

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
