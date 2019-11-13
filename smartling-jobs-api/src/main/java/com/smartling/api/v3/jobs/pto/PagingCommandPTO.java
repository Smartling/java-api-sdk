package com.smartling.api.v3.jobs.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;

/**
 * Specifies listing parameters like offset and limit to pagination of returned results.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingCommandPTO
{
    @QueryParam("offset")
    private Integer offset;

    @QueryParam("limit")
    private Integer limit;

}
