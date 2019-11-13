package com.smartling.api.v3.jobs.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomFieldAssignmentPTO implements ResponseData
{
    private String fieldUid;
}
