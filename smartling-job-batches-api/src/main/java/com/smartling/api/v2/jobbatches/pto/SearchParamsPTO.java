package com.smartling.api.v2.jobbatches.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class SearchParamsPTO
{
    @QueryParam("offset")
    private Integer offset;
    @QueryParam("limit")
    private Integer limit;
    @QueryParam("sortBy")
    private String sortBy ;
    @QueryParam("orderBy")
    private String orderBy;
    @QueryParam("status")
    private String status;
    @QueryParam("translationJobUid")
    private String translationJobUid;
}
