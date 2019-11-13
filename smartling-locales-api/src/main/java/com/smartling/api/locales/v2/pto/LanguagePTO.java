package com.smartling.api.locales.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LanguagePTO implements ResponseData
{
    private String languageId;
    private String description;
    private String direction;
    private String wordDelimiter;
}
