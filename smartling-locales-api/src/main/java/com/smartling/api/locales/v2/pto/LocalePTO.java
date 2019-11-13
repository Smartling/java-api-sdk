package com.smartling.api.locales.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Provides information about a locale.
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
