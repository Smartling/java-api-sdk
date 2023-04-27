package com.smartling.api.accounts.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectDetailsPTO implements ResponseData
{
    private String accountUid;
    private Boolean archived;
    private String packageUid;
    private String projectId;
    private String projectName;
    private String projectTypeCode;
    private String sourceLocaleDescription;
    private String sourceLocaleId;
}
