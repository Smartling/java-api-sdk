package com.smartling.api.jobs.v3.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WorkflowStepClassWordCountPTO extends WordCountPTO implements ResponseData
{
    private String workflowStepType;
    private String workflowStepName;

    public WorkflowStepClassWordCountPTO(long stringCount, long wordCount, String workflowStepType, String workflowStepName)
    {
        super(stringCount, wordCount);
        this.workflowStepType = workflowStepType;
        this.workflowStepName = workflowStepName;
    }
}
