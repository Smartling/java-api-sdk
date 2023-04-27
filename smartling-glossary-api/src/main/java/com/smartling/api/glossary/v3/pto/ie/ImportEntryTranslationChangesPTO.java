package com.smartling.api.glossary.v3.pto.ie;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Describes changes to glossary entries translations - when import process was initiated.
 */
@Data
@NoArgsConstructor
public class ImportEntryTranslationChangesPTO {
    /**
     * Translation locale.
     */
    private String localeId;
    /**
     * Amount of translations to be created.
     */
    private int newTranslations;
    /**
     * Amount of translations to be updated.
     */
    private int updatedTranslations;
    /**
     * Amount of translations to be removed.
     */
    private int translationsToRemove;

}
