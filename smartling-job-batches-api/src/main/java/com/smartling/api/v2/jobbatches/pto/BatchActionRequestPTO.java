package com.smartling.api.v2.jobbatches.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class BatchActionRequestPTO
{
    private String action = "execute";
    private List<WorkflowPTO> localeWorkflows;
}
