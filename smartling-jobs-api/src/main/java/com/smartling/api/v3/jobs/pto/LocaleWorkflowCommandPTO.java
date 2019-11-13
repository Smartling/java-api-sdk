package com.smartling.api.v3.jobs.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocaleWorkflowCommandPTO
{
    private String targetLocaleId;
    private String workflowUid;
}
