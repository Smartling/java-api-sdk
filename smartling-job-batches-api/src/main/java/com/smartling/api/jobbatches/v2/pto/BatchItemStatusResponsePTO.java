package com.smartling.api.jobbatches.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchItemStatusResponsePTO implements ResponseData
{
    private String status;
    private String generalErrors;
    private String fileUri;
    private String projectUid;
    private String batchUid;
}
