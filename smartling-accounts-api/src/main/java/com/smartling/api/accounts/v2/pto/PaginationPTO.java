package com.smartling.api.accounts.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.ws.rs.QueryParam;

/**
 * Standard Smartling limit and offset to paginate through results.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PaginationPTO
{
    /**
     * Standard Smartling offset to paginate through results.
     */
    @QueryParam("offset")
    private Integer offset;

    /**
     * Standard Smartling limit to paginate through results. 500 projects are returned by default if not specified.
     */
    @QueryParam("limit")
    private Integer limit;
}
