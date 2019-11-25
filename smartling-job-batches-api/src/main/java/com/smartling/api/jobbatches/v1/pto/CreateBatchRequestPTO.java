package com.smartling.api.jobbatches.v1.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBatchRequestPTO
{
    private String translationJobUid;
    private Boolean authorize;
}
