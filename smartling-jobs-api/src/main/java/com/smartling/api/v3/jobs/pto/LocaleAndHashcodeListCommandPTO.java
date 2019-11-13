package com.smartling.api.v3.jobs.pto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LocaleAndHashcodeListCommandPTO
{
    /**
     * Indicates, if strings should be moved from other jobs in that case.
     */
    private boolean moveEnabled = false;
    private List<String> hashcodes;
    private List<String> targetLocaleIds;
}
