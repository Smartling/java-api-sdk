package com.smartling.api.jobbatches.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelBatchActionRequestPTO
{
    private final String action = "CANCEL_FILE";
    private String fileUri;
    private String reason;
}
