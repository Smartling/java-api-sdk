package com.smartling.api.projects.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProjectDetailsPTO implements ResponseData
{
    private String accountUid;
    private Boolean archived;
    private String packageUid;
    private String projectId;
    private String projectName;
    private String projectTypeCode;
    private String projectTypeDisplayValue;
    private String sourceLocaleDescription;
    private String sourceLocaleId;
    private List<LocaleDetailsPTO> targetLocales;
}
