package com.smartling.api.files.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.ws.rs.QueryParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PagedFilter
{
    @QueryParam("offset")
    private Integer offset;

    @QueryParam("limit")
    private Integer limit;
}
