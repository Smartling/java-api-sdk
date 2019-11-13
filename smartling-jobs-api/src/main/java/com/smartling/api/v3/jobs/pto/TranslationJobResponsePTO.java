package com.smartling.api.v3.jobs.pto;

import com.smartling.api.v3.jobs.pto.account.IssuesCountsPTO;
import com.smartling.api.v2.response.ResponseData;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@NoArgsConstructor
@Accessors(chain = true)
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
