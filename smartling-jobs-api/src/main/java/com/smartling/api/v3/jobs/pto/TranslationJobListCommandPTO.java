package com.smartling.api.v3.jobs.pto;

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
public class TranslationJobListCommandPTO
{
    @QueryParam("jobName")
    private String jobName;

    @QueryParam("jobNumber")
    private String jobNumber;

    @QueryParam("translationJobUids")
    private List<String> translationJobUids;

    @QueryParam("translationJobStatus")
    private List<String> translationJobStatus;
}
