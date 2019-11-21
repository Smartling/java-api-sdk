package com.smartling.api.jobbatches.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterBatchActionRequestPTO
{
    private final String action = "REGISTER_FILE";
    private String fileUri;
}
