package com.smartling.glossary.v3.pto.entry.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * Glossary entry translation command.
 * Please note - that at least 'term', 'notes' values  or disabled - {@code true} should be set, otherwise operation will be failed.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlossaryEntryTranslationCommandPTO {
    /**
     * Translation locale id.
     */
    private String localeId;
    /**
     * Translation term. Max term length is equal to 250 characters.
     */
    private String term;
    /**
     * Translation notes. Max notes length is equal to 1500 characters.
     */
    private String notes;
    /**
     * Translation caseSensitive.
     */
    private boolean caseSensitive;
    /**
     * Translation exactMatch.
     */
    private boolean exactMatch;
    /**
     * Translation doNotTranslate.
     */
    private boolean doNotTranslate;
    /**
     * Translation disabled.
     */
    private boolean disabled;
    /**
     * Translation synonyms/variations.
     * Max length of single variation entry is 250 characters.
     * Max amount of variations in current array is 50.
     */
    private List<String> variants;
    /**
     * Translation assigned custom fields values.
     */
    private Collection<CustomFieldValuesCommandPTO> customFieldValues;
}
