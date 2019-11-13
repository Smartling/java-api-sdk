package com.smartling.api.v3.jobs.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkflowProgressReportPTO
{
    private String workflowUid;
    private String workflowName;
    @Singular("add")
    private List<WorkflowStepSummaryReportItemPTO> workflowStepSummaryReportItemList;
}
