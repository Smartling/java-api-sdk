package com.smartling.api.reports.v3.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordCountReportCommandPTO
{
    // Date Pattern: yyyy-MM-dd
    private String startDate;
    // Date Pattern: yyyy-MM-dd
    private String endDate;
    private String accountUid;
    @Singular
    private Set<String> projectIds;
    @Singular
    private Set<String> targetLocaleIds;
    @Singular
    private Set<String> userUids;
    private String agencyUid;
    @Singular
    private Set<String> jobUids;
    private Boolean includeTranslationResource;
    private Boolean includeJob;
    private Boolean includeJobReferenceNumber;
    private Boolean includeFuzzyMatchProfile;
    private Boolean includeWorkflowStep;
    private List<String> fields;
    private Integer limit;
    private int offset;
}
