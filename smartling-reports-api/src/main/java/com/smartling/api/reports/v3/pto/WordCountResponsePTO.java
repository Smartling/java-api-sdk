package com.smartling.api.reports.v3.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordCountResponsePTO implements ResponseData
{
    private String accountUid;
    private String accountName;
    private String projectId;
    private String projectName;
    private String targetLocaleId;
    private String targetLocale;
    private String jobUid;
    private String jobName;
    private String jobReferenceNumber;
    private String translationResourceUid;
    private String translationResourceName;
    private String agencyUid;
    private String agencyName;
    private String workflowStepType;
    private String workflowStepUid;
    private String workflowStepName;
    private String fuzzyProfileName;
    private String fuzzyTier;
    private Integer wordCount;
    private Integer weightedWordCount;
}
