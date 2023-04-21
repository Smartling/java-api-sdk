package com.smartling.glossary.v3.pto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import com.smartling.glossary.v3.pto.PagingCommandPTO;
import com.smartling.glossary.v3.pto.SortCommandPTO;

import java.util.List;

/**
 * Glossaries filter command.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GetGlossariesByFilterCommandPTO {
    /**
     * Allow to search glossaries by 'query' in scope of glossaryName / description properties.
     * Also supports exact search by 'glossaryUid'.
     */
    private String query;
    /**
     * Glossary state. One of:
     * - ACTIVE;
     * - ARCHIVED;
     * - BOTH.
     */
    private String glossaryState;
    /**
     * Filter glossaries that have passed locale - preconfigured.
     */
    private String targetLocaleId;
    /**
     * GlossaryUids filter.
     */
    private List<String> glossaryUids;
    /**
     * Paging command.
     */
    private PagingCommandPTO paging;
    /**
     * Sorting command.
     * Sort fields are - 'glossaryName | createdDate | lastModifiedDate'
     */
    private SortCommandPTO sorting;
}
