package com.smartling.api.contexts.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsyncProcessStartedPTO implements ResponseData
{
    private String processUid;
}
