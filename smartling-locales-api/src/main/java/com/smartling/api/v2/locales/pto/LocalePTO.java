package com.smartling.api.v2.locales.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PTO class to provide basic info about locale, which is Id and Description.
 */
@Data
@NoArgsConstructor
public class LocalePTO implements ResponseData
{
    private String localeId;
    private String description;
    private LanguagePTO language;
    private CountryPTO country;

    public LocalePTO(String localeId, String description)
    {
        this.localeId = localeId;
        this.description = description;
    }
}
