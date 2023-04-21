package com.smartling.glossary.v3.pto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Extends 'GetGlossariesByFilterCommandPTO' with additional glossaryEntries count property.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GetGlossariesWithEntriesCountByFilterCommandPTO extends GetGlossariesByFilterCommandPTO {
    /**
     * Allow to exclude / include glossary entries count, for each glossary that was found by the criteria.
     */
    private boolean includeEntriesCount;
}
