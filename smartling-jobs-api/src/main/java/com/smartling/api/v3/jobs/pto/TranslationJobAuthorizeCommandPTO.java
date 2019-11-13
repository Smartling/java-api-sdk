package com.smartling.api.v3.jobs.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslationJobAuthorizeCommandPTO
{
    private List<LocaleWorkflowCommandPTO> localeWorkflows;
    /**
     * @deprecated Ignored now, because we always refresh estimate and never "lock"
     */
    @Deprecated
    private String estimateUid;
}
