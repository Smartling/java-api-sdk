package com.smartling.api.jobs.v3.pto;

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
