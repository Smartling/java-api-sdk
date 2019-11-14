package com.smartling.api.jobs.v3.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowStepSummaryReportItemPTO
{
    private String workflowStepUid;
    private String workflowStepName;
    private long stringCount;
    private long wordCount;
    private String workflowStepType;
}
