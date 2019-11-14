package com.smartling.api.jobs.v3.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocaleAndHashcodeListCommandPTO
{
    /**
     * Indicates, if strings should be moved from other jobs in that case.
     */
    private boolean moveEnabled = false;
    private List<String> hashcodes;
    private List<String> targetLocaleIds;
}
