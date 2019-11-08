package com.smartling.api.v2.issues.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@Data
@EqualsAndHashCode
public class IssueStatePTO implements ResponseData
{
    private String issueStateCode;
    private String description;
}
