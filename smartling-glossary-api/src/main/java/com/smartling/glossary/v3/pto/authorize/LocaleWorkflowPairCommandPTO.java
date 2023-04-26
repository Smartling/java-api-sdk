package com.smartling.glossary.v3.pto.authorize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Locale-Workflow pairs for the authorization process to be started.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocaleWorkflowPairCommandPTO {
    /**
     * Locale id.
     */
    @NonNull
    private String localeId;
    /**
     * Workflow uid.
     */
    @NonNull
    private String workflowUid;

}
