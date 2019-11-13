package com.smartling.api.v3.jobs.pto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TranslationJobAddFileCommandPTO
{
    /**
     * Indicates, if strings should be moved from other jobs in that case.
     */
    private boolean moveEnabled = false;
    private String fileUri;
    private List<String> targetLocaleIds;

    public TranslationJobAddFileCommandPTO(boolean moveEnabled, String fileUri, List<String> targetLocaleIds)
    {
        this.moveEnabled = moveEnabled;
        this.fileUri = fileUri;
        this.targetLocaleIds = targetLocaleIds;
    }

    public TranslationJobAddFileCommandPTO(String fileUri, List<String> targetLocaleIds)
    {
        this.fileUri = fileUri;
        this.targetLocaleIds = targetLocaleIds;
    }

    public TranslationJobAddFileCommandPTO()
    {
    }
}
