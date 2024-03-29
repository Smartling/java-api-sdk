package com.smartling.api.glossary.v3.pto.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Extended translation progress meta inf.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EntryTranslationInProgressFullPTO extends EntryTranslationInProgressPTO {
    /**
     * Project identifier.
     */
    private String projectId;
    /**
     * Hash code of the submitted original string.
     */
    private String hashCode;
    /**
     * Translation jobUid.
     */
    private String jobUid;
}
