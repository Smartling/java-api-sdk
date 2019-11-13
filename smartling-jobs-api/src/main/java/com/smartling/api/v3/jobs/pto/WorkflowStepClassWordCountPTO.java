package com.smartling.api.v3.jobs.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class WorkflowStepClassWordCountPTO extends WordCountPTO implements ResponseData
{
    private String workflowStepType;

    public WorkflowStepClassWordCountPTO(long stringCount, long wordCount, String workflowStepType)
    {
        super(stringCount, wordCount);
        this.workflowStepType = workflowStepType;
    }
}
