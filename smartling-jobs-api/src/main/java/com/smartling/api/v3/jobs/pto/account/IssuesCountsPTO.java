package com.smartling.api.v3.jobs.pto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class IssuesCountsPTO
{
    private int sourceIssuesCount;
    private int translationIssuesCount;
}
