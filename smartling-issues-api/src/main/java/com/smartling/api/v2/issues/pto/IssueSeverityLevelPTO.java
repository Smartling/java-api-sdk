package com.smartling.api.v2.issues.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@Data
@EqualsAndHashCode
public class IssueSeverityLevelPTO implements ResponseData
{
    private String issueSeverityLevelCode;
}
