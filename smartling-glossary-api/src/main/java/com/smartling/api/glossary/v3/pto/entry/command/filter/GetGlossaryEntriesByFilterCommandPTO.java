package com.smartling.api.glossary.v3.pto.entry.command.filter;

import com.smartling.api.glossary.v3.pto.PagingCommandPTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

/**
 * Set of attributes used to perform filtering on a search of GlossaryEntry.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GetGlossaryEntriesByFilterCommandPTO {
    /**
     * Represents keyword for translation or variant match.
     */
    private String query;
    /**
     * List of locales - for translation filtering.
     */
    private List<String> localeIds;
    /**
     * Set of entryUids.
     */
    private Set<String> entryUids;
    /**
     * State of the entry.
     * OR archived, active, both filter.
     * Possible values :
     * - ACTIVE - only active;
     * - ARCHIVED - only archived;
     * - BOTH  - active + archived.
     */
    private String entryState;
    /**
     * As additional to {@link #localeIds} allow to filter empty/missing terms by incoming missing term locale id.
     */
    private String missingTranslationLocaleId;
    /**
     * As additional to {@link #localeIds} allow to filter non empty terms by incoming term locale id.
     */
    private String presentTranslationLocaleId;

    /**
     * As additional to {@link #localeIds} allow to filter translations marked as DNT by incoming translation locale id.
     */
    private String dntLocaleId;

    /**
     * For all translations, if they are missing, appropriate fallback locales will be used.
     */
    private boolean returnFallbackTranslations;
    /**
     * Labels filter.
     */
    private EntryLabelsFilterCommandPTO labels;
    /**
     * Return entries where dnt term is set.
     */
    private boolean dntTermSet;
    /**
     * Created date filter.
     */
    private AuditionDateCommandPTO created;
    /**
     * Last modified by filter.
     */
    private AuditionDateCommandPTO lastModified;
    /**
     * Created by filter.
     */
    private AuditorCommandPTO createdBy;
    /**
     * Last modified by filter.
     */
    private AuditorCommandPTO lastModifiedBy;
    /**
     * Paging data.
     */
    private PagingCommandPTO paging;
    /**
     * Sorting data.
     */
    private GlossaryEntriesSortCommandPTO sorting;

}
