package com.smartling.api.v3.jobs.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
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
