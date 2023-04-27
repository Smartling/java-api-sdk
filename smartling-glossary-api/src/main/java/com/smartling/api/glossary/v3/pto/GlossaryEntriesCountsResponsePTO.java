package com.smartling.api.glossary.v3.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entries and blocklists count response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlossaryEntriesCountsResponsePTO implements ResponseData {
    /**
     * Glossary uid.
     */
    private String glossaryUid;
    /**
     * Glossary entries amount.
     */
    private Integer entriesCount;
    /**
     * Attached blocklists amount.
     */
    private Integer blocklistsCount;
}
