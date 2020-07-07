package com.smartling.api.projects.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocaleDetailsPTO implements ResponseData
{
    private boolean enabled;
    private String localeId;
    private String description;
}
