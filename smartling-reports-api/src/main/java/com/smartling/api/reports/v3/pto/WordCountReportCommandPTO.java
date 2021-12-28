package com.smartling.api.reports.v3.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordCountReportCommandPTO
{
    // Date Pattern: yyyy-MM-dd
    @QueryParam("startDate")
    private String startDate;
    // Date Pattern: yyyy-MM-dd
    @QueryParam("endDate")
    private String endDate;
    @QueryParam("accountUid")
    private String accountUid;
    @Singular
    @QueryParam("projectIds")
    private Set<String> projectIds;
    @Singular
    @QueryParam("targetLocaleIds")
    private Set<String> targetLocaleIds;
    @Singular
    @QueryParam("userUids")
    private Set<String> userUids;
    @QueryParam("agencyUid")
    private String agencyUid;
    @Singular
    @QueryParam("jobUids")
    private Set<String> jobUids;
    @QueryParam("includeTranslationResource")
    private Boolean includeTranslationResource;
    @QueryParam("includeJob")
    private Boolean includeJob;
    @QueryParam("includeJobReferenceNumber")
    private Boolean includeJobReferenceNumber;
    @QueryParam("includeFuzzyMatchProfile")
    private Boolean includeFuzzyMatchProfile;
    @QueryParam("includeWorkflowStep")
    private Boolean includeWorkflowStep;
    @QueryParam("fields")
    private List<String> fields;
    @QueryParam("limit")
    private Integer limit;
    @QueryParam("offset")
    private int offset;
}
