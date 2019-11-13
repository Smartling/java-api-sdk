package com.smartling.api.v3.jobs.pto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class AsyncResponsePTO implements ResponseData
{
    private String message;
    private String url;
    private String processUid;
    @JsonIgnore
    public boolean isAsync()
    {
        return processUid != null && !processUid.isEmpty();
    }
}
