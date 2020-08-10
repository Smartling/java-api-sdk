package com.smartling.api.strings.v2.pto;


import java.io.Serializable;

public class StringCreationPTO implements Serializable
{
    private String format;
    private String variant;
    private String stringText;
    private String instruction;
    private String callbackUrl;
    private String callbackMethod;

    public String getFormat()
    {
        return format;
    }

    public void setFormat(String format)
    {
        this.format = format;
    }

    public String getVariant()
    {
        return variant;
    }

    public void setVariant(String variant)
    {
        this.variant = variant;
    }

    public String getStringText()
    {
        return stringText;
    }

    public void setStringText(String stringText)
    {
        this.stringText = stringText;
    }

    public String getInstruction()
    {
        return instruction;
    }

    public void setInstruction(String instruction)
    {
        this.instruction = instruction;
    }

    public String getCallbackUrl()
    {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl)
    {
        this.callbackUrl = callbackUrl;
    }

    public String getCallbackMethod()
    {
        return callbackMethod;
    }

    public void setCallbackMethod(String callbackMethod)
    {
        this.callbackMethod = callbackMethod;
    }
}
