package com.smartling.api.jobbatches.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBatchRequestPTO
{
    private String translationJobUid;
    private Boolean authorize;
    private List<String> fileUris;
    private List<WorkflowPTO> localeWorkflows;

    public CreateBatchRequestPTO(String translationJobUid, Boolean authorize, List<String> fileUris)
    {
        this(translationJobUid, authorize, fileUris, Collections.<WorkflowPTO>emptyList());
    }
}
