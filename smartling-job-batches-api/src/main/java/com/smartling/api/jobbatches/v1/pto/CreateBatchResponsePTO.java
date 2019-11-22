package com.smartling.api.jobbatches.v1.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBatchResponsePTO implements ResponseData
{
    private String batchUid;
}
