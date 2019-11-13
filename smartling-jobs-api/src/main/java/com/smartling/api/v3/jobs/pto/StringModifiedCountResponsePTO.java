package com.smartling.api.v3.jobs.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StringModifiedCountResponsePTO extends AsyncResponsePTO
{
    private int    successCount;
    private int    failCount;
}
