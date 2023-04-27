package com.smartling.api.glossary.v3.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Glossary response with entries count.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GlossarySearchResponsePTO extends GlossaryResponsePTO {
    /**
     * Amount of entries in current glossary.
     */
    private Integer entriesCount;
}
