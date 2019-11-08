package com.smartling.api.v2.issues.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
@SuperBuilder
public class IssuesPagingFilterPTO extends IssuesFilterPTO
{
    private Integer offset;
    private Integer limit;
}
