package com.smartling.api.v3.jobs.pto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class WorkflowProgressReportPTO
{
    private String workflowUid;
    private String workflowName;
    @Singular("add")
    private List<WorkflowStepSummaryReportItemPTO> workflowStepSummaryReportItemList;
}
