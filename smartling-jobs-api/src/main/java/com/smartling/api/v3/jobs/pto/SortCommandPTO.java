package com.smartling.api.v3.jobs.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.ws.rs.QueryParam;

/**
 * Specifies listing parameters like sortBy and sortDirection to pagination of returned results.
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SortCommandPTO implements Sortable
{
    @QueryParam("sortBy")
    private String sortBy;

    @QueryParam("sortDirection")
    private String sortDirection;
}
