package com.smartling.api.jobbatches.v1.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchActionRequestPTO
{
    private final String action = "execute";
    private List<WorkflowPTO> localeWorkflows;
}
