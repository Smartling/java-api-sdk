package com.smartling.api.filetranslations.v2.pto.mt;

import com.smartling.api.filetranslations.v2.pto.Error;
import java.util.Map;

public class MtLocaleStatus
{
    private String localeId;
    private MtState state;
    private Long processedStringCount;
    private Error<Map<String, String>> error;
    public MtLocaleStatus()
    {
    }

    public MtLocaleStatus(String localeId, MtState state, Long processedStringCount, Error<Map<String, String>> error)
    {
        this.localeId = localeId;
        this.state = state;
        this.processedStringCount = processedStringCount;
        this.error = error;
    }

    public String getLocaleId()
    {
        return localeId;
    }

    public void setLocaleId(String localeId)
    {
        this.localeId = localeId;
    }

    public MtState getState()
    {
        return state;
    }

    public void setState(MtState state)
    {
        this.state = state;
    }

    public Long getProcessedStringCount()
    {
        return processedStringCount;
    }

    public void setProcessedStringCount(Long processedStringCount)
    {
        this.processedStringCount = processedStringCount;
    }

    public Error<Map<String, String>> getError()
    {
        return error;
    }

    public void setError(Error<Map<String, String>> error)
    {
        this.error = error;
    }

    @Override
    public String toString()
    {
        return "MtLocaleStatus{" +
            "localeId='" + localeId + '\'' +
            ", state=" + state +
            ", processedStringCount=" + processedStringCount +
            ", error=" + error +
            '}';
    }
}
