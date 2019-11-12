package com.smartling.api.v2.locales.pto;

import com.smartling.api.v2.response.ResponseData;

/**
 * PTO class to provide basic info about locale, which is Id and Description.
 */
public class LocalePTO implements ResponseData
{
    private String localeId;
    private String description;
    private LanguagePTO language;
    private CountryPTO country;

    public LocalePTO()
    {
    }

    public LocalePTO(String localeId, String description)
    {
        this.localeId = localeId;
        this.description = description;
    }

    public String getLocaleId()
    {
        return localeId;
    }

    public void setLocaleId(String localeId)
    {
        this.localeId = localeId;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public LanguagePTO getLanguage()
    {
        return language;
    }

    public void setLanguage(LanguagePTO language)
    {
        this.language = language;
    }

    public CountryPTO getCountry()
    {
        return country;
    }

    public void setCountry(CountryPTO country)
    {
        this.country = country;
    }
}
