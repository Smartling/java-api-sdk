package com.smartling.api.glossary.v3.pto.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * Glossary entry response.
 * Which provides translation request details in minimal form.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GlossaryEntryResponsePTO extends GlossaryEntryBaseResponsePTO {
    /**
     * Request translation details in min form.
     */
    private Map<String, EntryTranslationInProgressPTO> requestTranslationStatuses;
}
