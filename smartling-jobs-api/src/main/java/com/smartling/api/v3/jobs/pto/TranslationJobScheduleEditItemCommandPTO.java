package com.smartling.api.v3.jobs.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TranslationJobScheduleEditItemCommandPTO
{
    private String targetLocaleId;
    private String workflowStepUid;
    private String dueDate;
}
