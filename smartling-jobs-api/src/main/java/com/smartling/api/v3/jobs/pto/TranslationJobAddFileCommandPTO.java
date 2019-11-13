package com.smartling.api.v3.jobs.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TranslationJobAddFileCommandPTO
{
    /**
     * Indicates, if strings should be moved from other jobs in that case.
     */
    private boolean moveEnabled = false;
    private String fileUri;
    private List<String> targetLocaleIds;

    public TranslationJobAddFileCommandPTO(String fileUri, List<String> targetLocaleIds)
    {
        this.fileUri = fileUri;
        this.targetLocaleIds = targetLocaleIds;
    }

}
