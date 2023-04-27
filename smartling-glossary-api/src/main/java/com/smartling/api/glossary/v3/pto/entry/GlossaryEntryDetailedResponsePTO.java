package com.smartling.api.glossary.v3.pto.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * Detailed glossary entry response.
 * Which provides translation request details in extended form.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GlossaryEntryDetailedResponsePTO extends GlossaryEntryBaseResponsePTO {
    /**
     * Request translation details in extended form.
     */
    private Map<String, EntryTranslationInProgressFullPTO> requestTranslationStatuses;
}
