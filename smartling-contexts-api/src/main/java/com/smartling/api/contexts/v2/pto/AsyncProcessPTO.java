package com.smartling.api.contexts.v2.pto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsyncProcessPTO implements ResponseData
{
    private String processUid;
    private AsyncProcessState processState;
    private AsyncProcessType processType;
    private String createdDate;
    private String modifiedDate;
    private String errorMessage;
    @JsonDeserialize(contentAs = MatchAsyncProcessResultPTO.class)
    private AsyncProcessResultPTO result;
}
