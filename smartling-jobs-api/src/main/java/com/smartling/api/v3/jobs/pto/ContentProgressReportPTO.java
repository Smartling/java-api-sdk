package com.smartling.api.v3.jobs.pto;

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
public class ContentProgressReportPTO implements ResponseData
{
    @Singular("add")
    private List<LocaleContentProgressReportPTO> contentProgressReport;
    private JobProgressCompletedPTO progress;
    private List<WorkflowStepClassWordCountPTO> summaryReport;

    public ContentProgressReportPTO(List<LocaleContentProgressReportPTO> contentProgressReport)
    {
        this.contentProgressReport = contentProgressReport;
    }
}
