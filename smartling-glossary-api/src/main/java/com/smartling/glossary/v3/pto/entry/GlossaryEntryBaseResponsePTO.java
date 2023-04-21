package com.smartling.glossary.v3.pto.entry;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

/**
 * Glossary entry response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GlossaryEntryBaseResponsePTO implements ResponseData {
    /**
     * Glossary entry uid.
     */
    private String entryUid;
    /**
     * Glossary uid.
     */
    private String glossaryUid;
    /**
     * Glossary entry definition.
     */
    private String definition;
    /**
     * Glossary entry part of speech value.
     */
    private String partOfSpeech;
    /**
     * Assigned label uuids.
     */
    private Set<String> labelUids;
    /**
     * Translations collection.
     */
    private List<GlossaryEntryTranslationPTO> translations;
    /**
     * Assigned custom field values.
     */
    private List<EntryCustomFieldValuePTO> customFieldValues;
    /**
     * Archived flag.
     */
    private boolean archived;
    /**
     * Uid of the creator.
     */
    private String createdByUserUid;
    /**
     * Uid of the user who has performed latest modification.
     */
    private String modifiedByUserUid;
    /**
     * Date of creation of the entry.
     */
    private String createdDate;
    /**
     * Entry last modification date.
     */
    private String modifiedDate;
}
