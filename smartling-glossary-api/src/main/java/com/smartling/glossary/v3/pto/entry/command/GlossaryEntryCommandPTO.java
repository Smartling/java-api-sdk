package com.smartling.glossary.v3.pto.entry.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Set;

/**
 * Glossary entry command.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlossaryEntryCommandPTO {
    /**
     * Glossary entry definition. Max chars amount is equal to 1500.
     */
    private String definition;
    /**
     * Glossary entry part of speech.
     * One of :
     * - Noun,
     * - Verb,
     * - Adjective,
     * - Adverb,
     * - Pronoun,
     * - Preposition,
     * - Interjection,
     * - Conjunction,
     * - Proper Noun.
     */
    private String partOfSpeech;
    /**
     * Label uids.
     */
    private Set<String> labelUids;
    /**
     * Glossary entry translations.
     * Please note :
     * - in case if glossary entry already has some translation, they will be removed if not present in current array - during update procedure.
     */
    private Collection<GlossaryEntryTranslationCommandPTO> translations;
    /**
     * Assigned custom field values.
     */
    private Collection<CustomFieldValuesCommandPTO> customFieldValues;
}

