package com.smartling.api.jobbatches.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchParamsPTO
{
    @QueryParam("offset")
    private Integer offset;

    @QueryParam("limit")
    private Integer limit;

    @QueryParam("sortBy")
    private String sortBy;

    @QueryParam("orderBy")
    private String orderBy;

    @QueryParam("status")
    private String status;

    @QueryParam("translationJobUid")
    private String translationJobUid;
}
