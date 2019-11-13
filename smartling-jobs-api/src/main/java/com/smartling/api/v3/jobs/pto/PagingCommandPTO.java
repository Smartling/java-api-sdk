package com.smartling.api.v3.jobs.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.ws.rs.QueryParam;

/**
 * Specifies listing parameters like offset and limit to pagination of returned results.
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PagingCommandPTO
{
    @QueryParam("offset")
    private Integer offset;

    @QueryParam("limit")
    private Integer limit;

}
