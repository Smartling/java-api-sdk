package com.smartling.api.glossary.v3.pto.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Translation progress meta inf.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EntryTranslationInProgressPTO {
    /**
     * Locale id.
     */
    private String localeId;
    /**
     * Translation request date-time.
     */
    private String requestDateTime;
    /**
     * Translation request user uid.
     */
    private String requestUserUid;
}
