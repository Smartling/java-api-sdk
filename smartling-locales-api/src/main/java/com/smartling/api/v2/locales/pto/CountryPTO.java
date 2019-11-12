package com.smartling.api.v2.locales.pto;

import com.smartling.api.v2.response.ResponseData;

public class CountryPTO implements ResponseData
{
    private String countryId;
    private String description;

    public String getCountryId()
    {
        return countryId;
    }

    public void setCountryId(String countryId)
    {
        this.countryId = countryId;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
