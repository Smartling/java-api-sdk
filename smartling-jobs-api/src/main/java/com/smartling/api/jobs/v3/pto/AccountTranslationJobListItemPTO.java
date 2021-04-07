package com.smartling.api.jobs.v3.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountTranslationJobListItemPTO implements ResponseData
{
    private String translationJobUid;
    private String jobName;
    private String jobNumber;
    private String dueDate;
    private List<String> targetLocaleIds;
    private String createdDate;
    private String jobStatus;
    private String referenceNumber;
    private String description;
    private String projectId;
    private Long priority;
}
