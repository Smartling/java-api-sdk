package com.smartling.api.jobs.v3.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TranslationJobUpdateCommandPTO
{
    private String jobName;
    private String description;
    private String dueDate;
    private String referenceNumber;
    private String callbackUrl;
    private String callbackMethod;
    private List<TranslationJobCustomFieldPTO> customFields;

    public TranslationJobUpdateCommandPTO(String jobName, String description, String dueDate, String referenceNumber, String callbackUrl,
                                          String callbackMethod)
    {
        this.jobName = jobName;
        this.description = description;
        this.dueDate = dueDate;
        this.referenceNumber = referenceNumber;
        this.callbackUrl = callbackUrl;
        this.callbackMethod = callbackMethod;
    }
}
