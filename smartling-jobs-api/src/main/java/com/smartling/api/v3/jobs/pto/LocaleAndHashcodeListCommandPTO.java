package com.smartling.api.v3.jobs.pto;

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
