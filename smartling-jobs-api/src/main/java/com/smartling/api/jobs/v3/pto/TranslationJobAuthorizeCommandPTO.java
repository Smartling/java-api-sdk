package com.smartling.api.jobs.v3.pto;

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
}
