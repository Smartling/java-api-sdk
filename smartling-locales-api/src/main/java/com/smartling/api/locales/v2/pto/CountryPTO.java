package com.smartling.api.locales.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CountryPTO implements ResponseData
{
    private String countryId;
    private String description;
}
