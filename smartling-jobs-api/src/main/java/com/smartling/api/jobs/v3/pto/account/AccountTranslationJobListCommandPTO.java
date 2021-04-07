package com.smartling.api.jobs.v3.pto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountTranslationJobListCommandPTO
{
    @QueryParam("jobName")
    private String jobName;

    @QueryParam("withPriority")
    private boolean withPriority;

    @QueryParam("projectIds")
    private List<String> projectIds;

    @QueryParam("translationJobStatus")
    private List<String> translationJobStatus;
}
