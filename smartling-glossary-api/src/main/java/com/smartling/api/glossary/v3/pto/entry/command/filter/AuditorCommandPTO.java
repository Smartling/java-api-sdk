package com.smartling.api.glossary.v3.pto.entry.command.filter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represent command that allow filtering by auditor related properties.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AuditorCommandPTO {
    /**
     * Allow defining on which level ( entry / translation ) filtering should be performed.
     * USE  values:
     * <p>
     * ENTRY - to filter on glossary entry level,
     * <p>
     * LOCALE - to filter on glossary entry translation level,
     * <p>
     * ANY - to filter on both levels ( this is default ).
     */
    private String level;
    /**
     * List of user identifiers.
     */
    private List<String> userIds;

}
