package com.smartling.api.v2.issues.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@AllArgsConstructor
@Data
@EqualsAndHashCode
public class IssueTextPTO implements ResponseData
{
    private String issueText;
    private Date issueTextModifiedDate;
}
