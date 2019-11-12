package com.smartling.api.v2.locales.pto;

import com.smartling.api.v2.response.ResponseData;

public class LanguagePTO implements ResponseData
{
    private String languageId;
    private String description;
    private String direction;
    private String wordDelimiter;

    public String getLanguageId()
    {
        return languageId;
    }

    public void setLanguageId(String languageId)
    {
        this.languageId = languageId;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDirection()
    {
        return direction;
    }

    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    public String getWordDelimiter()
    {
        return wordDelimiter;
    }

    public void setWordDelimiter(String wordDelimiter)
    {
        this.wordDelimiter = wordDelimiter;
    }
}
