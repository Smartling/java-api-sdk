package com.smartling.api.glossary.v3.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * QueryParam annotation added to make possible use of passing params through GET request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SortCommandPTO {
    /**
     * Sort field.
     */
    private String field;
    /**
     * Sort direction.
     * One of:
     *  - ASC;
     *  - DESC.
     */
    private String direction;
}
