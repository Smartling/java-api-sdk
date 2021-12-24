package com.smartling.api.jobs.v3.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WorkflowStepClassNameWordCountPTO extends WorkflowStepClassWordCountPTO
{
    private String workflowStepName;

    public WorkflowStepClassNameWordCountPTO(long stringCount, long wordCount, String workflowStepType, String workflowStepName)
    {
        super(stringCount, wordCount, workflowStepType);
        this.workflowStepName = workflowStepName;
    }
}
