package com.smartling.api.jobbatches.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelBatchActionRequestPTO
{
    private String action = "CANCEL_FILE";
    private String fileUri;
    private String reason;
}
