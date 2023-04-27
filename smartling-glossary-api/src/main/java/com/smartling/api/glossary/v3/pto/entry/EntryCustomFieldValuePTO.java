package com.smartling.api.glossary.v3.pto.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Custom field value.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EntryCustomFieldValuePTO
{
    /**
     * Custom field uid.
     */
    private String fieldUid;
    /**
     * Custom field name.
     */
    private String fieldName;
    /**
     * Custom field value.
     */
    private String fieldValue;
}
