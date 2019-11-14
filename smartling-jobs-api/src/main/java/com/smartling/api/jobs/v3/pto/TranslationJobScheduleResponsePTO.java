package com.smartling.api.jobs.v3.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TranslationJobScheduleResponsePTO implements ResponseData
{
    private String scheduleUid;
    private String targetLocaleId;
    private String workflowStepUid;
    private String dueDate;
}
