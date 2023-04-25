package com.smartling.api.filetranslations.v2.pto.mt;


import com.smartling.api.filetranslations.v2.pto.Error;
import com.smartling.api.v2.response.ResponseData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MtStatusResponse implements ResponseData
{
    private MtState state;
    private Long requestedStringCount;
    private Error<Map<String,String>> error;
    private List<MtLocaleStatus> localeProcessStatuses = new ArrayList<>();
    public MtStatusResponse()
    {
    }

    public MtStatusResponse(MtState state, Long requestedStringCount, Error<Map<String, String>> error,
            List<MtLocaleStatus> localeProcessStatuses)
    {
        this.state = state;
        this.requestedStringCount = requestedStringCount;
        this.error = error;
        this.localeProcessStatuses = localeProcessStatuses;
    }

    public MtState getState()
    {
        return state;
    }

    public void setState(MtState state)
    {
        this.state = state;
    }

    public Long getRequestedStringCount()
    {
        return requestedStringCount;
    }

    public void setRequestedStringCount(Long requestedStringCount)
    {
        this.requestedStringCount = requestedStringCount;
    }

    public Error<Map<String, String>> getError()
    {
        return error;
    }

    public void setError(Error<Map<String, String>> error)
    {
        this.error = error;
    }

    public List<MtLocaleStatus> getLocaleProcessStatuses()
    {
        return localeProcessStatuses;
    }

    public void setLocaleProcessStatuses(List<MtLocaleStatus> localeProcessStatuses)
    {
        this.localeProcessStatuses = localeProcessStatuses;
    }

    @Override
    public String toString()
    {
        return "MtStatusResponse{" +
            "state=" + state +
            ", requestedStringCount=" + requestedStringCount +
            ", error=" + error +
            ", localeProcessStatuses=" + localeProcessStatuses +
            '}';
    }
}
