package com.smartling.api.v3.jobs.pto;

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

    public WorkflowStepClassWordCountPTO(long stringCount, long wordCount, String workflowStepType)
    {
        super(stringCount, wordCount);
        this.workflowStepType = workflowStepType;
    }
}
