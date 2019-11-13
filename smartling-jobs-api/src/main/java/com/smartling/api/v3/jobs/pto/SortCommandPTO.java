package com.smartling.api.v3.jobs.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;

/**
 * Specifies listing parameters like sortBy and sortDirection to pagination of returned results.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SortCommandPTO implements Sortable
{
    @QueryParam("sortBy")
    private String sortBy;

    @QueryParam("sortDirection")
    private String sortDirection;
}
