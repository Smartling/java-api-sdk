package com.smartling.api.strings.v2.pto;


import com.smartling.api.v2.response.ResponseData;

import java.util.List;

public class SourceStringPTO implements ResponseData
{
    private String hashcode;
    private String stringText;
    private String parsedStringText;
    private String stringVariant;

    private Integer maxLength;
    private List<StringKeyPTO> keys;

    public String getHashcode()
    {
        return hashcode;
    }

    public void setHashcode(String hashcode)
    {
        this.hashcode = hashcode;
    }

    public String getStringText()
    {
        return stringText;
    }

    public void setStringText(String stringText)
    {
        this.stringText = stringText;
    }

    public String getParsedStringText()
    {
        return parsedStringText;
    }

    public void setParsedStringText(String parsedStringText)
    {
        this.parsedStringText = parsedStringText;
    }

    public String getStringVariant()
    {
        return stringVariant;
    }

    public void setStringVariant(String stringVariant)
    {
        this.stringVariant = stringVariant;
    }

    public Integer getMaxLength()
    {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength)
    {
        this.maxLength = maxLength;
    }

    public List<StringKeyPTO> getKeys()
    {
        return keys;
    }

    public void setKeys(List<StringKeyPTO> keys)
    {
        this.keys = keys;
    }
}
