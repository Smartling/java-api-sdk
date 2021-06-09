package com.smartling.api.jobs.v3.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCustomFieldFilterPTO
{
    @QueryParam("includeDisabled")
    private boolean includeDisabled;
}
