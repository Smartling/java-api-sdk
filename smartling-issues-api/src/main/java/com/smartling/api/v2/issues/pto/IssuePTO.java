package com.smartling.api.v2.issues.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class IssuePTO implements ResponseData
{
    private String issueUid;
    private StringPTO string;
    private String resolvedByUserUid;
    private String reportedByUserUid;
    private String assigneeUserUid;
    private Date createdDate;
    private Date resolvedDate;
    private Date issueTextModifiedDate;
    private String issueText;
    private String projectId;
    private String issueTypeCode;
    private String issueSubTypeCode;
    private String issueStateCode;
    private Boolean answered;
    private String issueSeverityLevelCode;
}
