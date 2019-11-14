package com.smartling.api.jobs.v3.pto;

import com.smartling.api.v2.response.ResponseData;
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
public class LocaleContentProgressReportPTO implements ResponseData
{
    private String targetLocaleId;
    private String targetLocaleDescription;
    private UnauthorizedProgressReportPTO unauthorizedProgressReport;
    @Singular("add")
    private List<WorkflowProgressReportPTO> workflowProgressReportList;
    private JobProgressCompletedPTO progress;
    private List<WorkflowStepClassWordCountPTO> summaryReport;
}
