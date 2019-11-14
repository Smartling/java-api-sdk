package com.smartling.api.jobs.v3.pto;

import com.smartling.api.jobs.v3.pto.account.IssuesCountsPTO;
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
public class TranslationJobResponsePTO implements ResponseData
{
    private String translationJobUid;
    private String jobName;
    private String jobNumber;
    private List<String> targetLocaleIds;
    private String description;
    private String dueDate;
    private String referenceNumber;
    private String callbackUrl;
    private String callbackMethod;
    private String createdDate;
    private String modifiedDate;
    private String createdByUserUid;
    private String modifiedByUserUid;
    private String firstCompletedDate;
    private String lastCompletedDate;
    private String jobStatus;
    private IssuesCountsPTO issues;
    private List<TranslationJobCustomFieldPTO> customFields;
}
