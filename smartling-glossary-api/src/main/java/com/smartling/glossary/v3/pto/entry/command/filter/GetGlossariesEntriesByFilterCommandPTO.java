package com.smartling.glossary.v3.pto.entry.command.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Extends common glossary entries filter {@link GetGlossaryEntriesByFilterCommandPTO} with glossaryUids, to allow filtering by criteria in scope of multiple glossaries.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GetGlossariesEntriesByFilterCommandPTO extends GetGlossaryEntriesByFilterCommandPTO {
    /**
     * Glossary uids for the entries search.
     */
    private List<String> glossaryUids;
}
