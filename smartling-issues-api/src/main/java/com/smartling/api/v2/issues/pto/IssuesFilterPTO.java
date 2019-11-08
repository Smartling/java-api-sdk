package com.smartling.api.v2.issues.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@SuperBuilder
public class IssuesFilterPTO
{
    private Collection<String> issueStateCodes;
    private Collection<String> issueTypeCodes;
    private Collection<String> issueSubTypeCodes;
    private Timestamp createdDateAfter;
    private Timestamp createdDateBefore;
    private Timestamp resolvedDateAfter;
    private Timestamp resolvedDateBefore;
    private String reportedByUserUid;
    private StringFilterPTO stringFilter;
}
