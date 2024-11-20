package com.smartling.api.jobbatches.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowPTO
{
    private String targetLocaleId;
    private String workflowUid;
    private List<ContentAssignmentPTO> contentAssignments;
}
