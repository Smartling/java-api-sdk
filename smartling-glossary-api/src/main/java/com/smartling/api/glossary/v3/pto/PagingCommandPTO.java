package com.smartling.api.glossary.v3.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Set of attributes used to perform paging on a search of entities.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingCommandPTO {
    /**
     * Amount of items to skip.
     */
    private int offset;
    /**
     * Amount of items to fetch.
     */
    private int limit;
}
