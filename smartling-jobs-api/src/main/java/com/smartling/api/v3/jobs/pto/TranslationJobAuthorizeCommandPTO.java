package com.smartling.api.v3.jobs.pto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TranslationJobAuthorizeCommandPTO
{
    private List<LocaleWorkflowCommandPTO> localeWorkflows;
    /**
     * @deprecated Ignored now, because we always refresh estimate and never "lock"
     */
    @Deprecated
    private String estimateUid;
}
