package com.smartling.glossary.v3.pto.authorize;

import com.smartling.glossary.v3.pto.entry.command.filter.GetGlossaryEntriesByFilterCommandPTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Singular;

import java.util.List;

/**
 * Authorize entries command.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntriesAuthorizationCommandPTO {

    /**
     * Source locale identifier.
     */
    @NonNull
    private String sourceLocale;

    /**
     * Project id, where authorized content should be placed.
     */
    @NonNull
    private String projectId;

    /**
     * Entries/translation filter of the data to be authorized.
     */
    @NonNull
    private GetGlossaryEntriesByFilterCommandPTO filter;

    /**
     * Locale workflow pairs for the authorization process.
     */
    @NonNull
    @Singular
    private List<LocaleWorkflowPairCommandPTO> localeWorkflows;
}
