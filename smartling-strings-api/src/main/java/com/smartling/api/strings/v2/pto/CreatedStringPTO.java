package com.smartling.api.strings.v2.pto;


public class CreatedStringPTO
{
    private String variant;
    private String hashcode;
    private String stringText;
    private String parsedStringText;
    private boolean overWritten = false;

    public String getStringText()
    {
        return stringText;
    }

    public String getVariant()
    {
        return variant;
    }

    public String getHashcode()
    {
        return hashcode;
    }

    public CreatedStringPTO setStringText(String stringText)
    {
        this.stringText = stringText;
        return this;
    }

    public CreatedStringPTO setVariant(String variant)
    {
        this.variant = variant;
        return this;
    }

    public CreatedStringPTO setHashcode(String hashcode)
    {
        this.hashcode = hashcode;
        return this;
    }

    public boolean isOverWritten()
    {
        return overWritten;
    }

    public CreatedStringPTO setOverWritten(boolean overWritten)
    {
        this.overWritten = overWritten;
        return this;
    }

    public String getParsedStringText()
    {
        return parsedStringText;
    }

    public CreatedStringPTO setParsedStringText(String parsedStringText)
    {
        this.parsedStringText = parsedStringText;
        return this;
    }
}
