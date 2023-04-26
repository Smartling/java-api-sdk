package com.smartling.api.filetranslations.v2.pto.mt;

import java.util.List;

public class MtRequest
{
    private String sourceLocaleId;
    private List<String> targetLocaleIds;

    public MtRequest()
    {
    }

    public MtRequest(String sourceLocaleId, List<String> targetLocaleIds)
    {
        this.sourceLocaleId = sourceLocaleId;
        this.targetLocaleIds = targetLocaleIds;
    }

    public String getSourceLocaleId()
    {
        return sourceLocaleId;
    }

    public void setSourceLocaleId(String sourceLocaleId)
    {
        this.sourceLocaleId = sourceLocaleId;
    }

    public List<String> getTargetLocaleIds()
    {
        return targetLocaleIds;
    }

    public void setTargetLocaleIds(List<String> targetLocaleIds)
    {
        this.targetLocaleIds = targetLocaleIds;
    }

    @Override
    public String toString()
    {
        return "MtRequest{" +
            "sourceLocaleId='" + sourceLocaleId + '\'' +
            ", targetLocaleIds=" + targetLocaleIds +
            '}';
    }
}
