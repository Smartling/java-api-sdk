package com.smartling.api.v2.issues.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class IssueTemplatePTO
{
    private StringTemplatePTO string;
    private String issueTypeCode;
    private String issueSubTypeCode;
    private String issueText;
    private String issueSeverityLevelCode;

}
