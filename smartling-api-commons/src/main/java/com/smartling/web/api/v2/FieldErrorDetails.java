package com.smartling.web.api.v2;

public class FieldErrorDetails extends Details
{
    private String field;

    public FieldErrorDetails()
    {
    }

    public FieldErrorDetails(String field)
    {
        this.field = field;
    }

    public String getField()
    {
        return field;
    }

    public void setField(String field)
    {
        this.field = field;
    }
}
