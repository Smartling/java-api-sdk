package com.smartling.api.v2.jobbatches.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class WorkflowPTO
{
    private String targetLocaleId;
    private String workflowUid;
}
