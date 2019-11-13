package com.smartling.api.v3.jobs.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TranslationJobCreateCommandPTO
{
    private String jobName;
    private List<String> targetLocaleIds;
    private String description;
    private String dueDate;
    private String referenceNumber;
    private String callbackUrl;
    private String callbackMethod;
    private List<TranslationJobCustomFieldPTO> customFields;

    public TranslationJobCreateCommandPTO(String jobName, List<String> targetLocaleIds, String description, String dueDate, String referenceNumber,
                                          String callbackUrl, String callbackMethod)
    {
        this.jobName = jobName;
        this.targetLocaleIds = targetLocaleIds;
        this.description = description;
        this.dueDate = dueDate;
        this.referenceNumber = referenceNumber;
        this.callbackUrl = callbackUrl;
        this.callbackMethod = callbackMethod;
    }
}
