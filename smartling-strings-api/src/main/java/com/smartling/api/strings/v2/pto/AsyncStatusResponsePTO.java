package com.smartling.api.strings.v2.pto;


import com.smartling.api.v2.response.ResponseData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AsyncStatusResponsePTO implements ResponseData
{
    private String processUid;
    private String processState;
    private String createdDate;
    private String modifiedDate;
    private ProcessStatisticsPTO processStatistics;
}
