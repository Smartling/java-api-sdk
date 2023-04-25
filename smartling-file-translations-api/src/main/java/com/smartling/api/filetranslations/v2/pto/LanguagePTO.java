package com.smartling.api.filetranslations.v2.pto;

public class LanguagePTO
{
    private String languageId;
    private String defaultLocaleId;

    public LanguagePTO()
    {
        
    }
    
    public LanguagePTO(String languageId, String defaultLocaleId)
    {
        this.languageId = languageId;
        this.defaultLocaleId = defaultLocaleId;
    }
    public String getLanguageId()
    {
        return languageId;
    }

    public void setLanguageId(String languageId)
    {
        this.languageId = languageId;
    }

    public String getDefaultLocaleId()
    {
        return defaultLocaleId;
    }

    public void setDefaultLocaleId(String defaultLocaleId)
    {
        this.defaultLocaleId = defaultLocaleId;
    }

    @Override
    public String toString()
    {
        return "LanguagePTO{" +
            "languageId='" + languageId + '\'' +
            ", defaultLocaleId='" + defaultLocaleId + '\'' +
            '}';
    }
}
