package com.smartling.glossary.v3.pto.entry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Glossary entry translation.
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GlossaryEntryTranslationPTO {
    /**
     * Translation locale id.
     */
    private String localeId;
    /**
     * Translation fallback locale id.
     * Present only in case if fallback rules are configured, and current was mapped from the fallback.
     */
    private String fallbackLocaleId;
    /**
     * Glossary entry translation term.
     */
    private String term;
    /**
     * Glossary translation notes.
     */
    private String notes;
    /**
     * Glossary entry translation caseSensitive.
     */
    private boolean caseSensitive;
    /**
     * Glossary entry translation exactMatch.
     */
    private boolean exactMatch;
    /**
     * Glossary entry translation doNotTranslate.
     */
    private boolean doNotTranslate;
    /**
     * Glossary entry translation disabled.
     * Allow to enable/disable filtering out current translation from cat-tool-matching in case if it's disabled and translation locale is equals to current.
     */
    private boolean disabled;
    /**
     * Term variants/synonyms collection.
     */
    private List<String> variants;
    /**
     * Translation level, assigned custom fields values.
     */
    private List<EntryTranslationCustomFieldValuePTO> customFieldValues;
    /**
     * Status/progress of the translation, in case if was requested.
     */
    private EntryTranslationInProgressPTO requestTranslationStatus;
    /**
     * Uid of the creator.
     */
    private String createdByUserUid;
    /**
     * Uid of the user who has performed latest modification.
     */
    private String modifiedByUserUid;
    /**
     * Date of creation of the translation.
     */
    private String createdDate;
    /**
     * Translation last modification date.
     */
    private String modifiedDate;
}
