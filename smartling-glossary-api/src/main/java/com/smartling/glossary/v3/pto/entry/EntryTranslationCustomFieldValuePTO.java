package com.smartling.glossary.v3.pto.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Translation level custom field value.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class EntryTranslationCustomFieldValuePTO extends EntryCustomFieldValuePTO {
    /**
     * Translation locale id, for which current custom filed value belongs.
     */
    private String localeId;
}
