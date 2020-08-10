package com.smartling.api.strings.v2.pto;


import com.smartling.api.v2.response.ResponseData;

public class AsyncStatusResponsePTO implements ResponseData
{
    private String processUid;
    private String processState;
    private String createdDate;
    private String modifiedDate;
    private ProcessStatisticsPTO processStatistics;

    public String getProcessUid()
    {
        return processUid;
    }

    public void setProcessUid(final String processUid)
    {
        this.processUid = processUid;
    }

    public String getProcessState()
    {
        return processState;
    }

    public void setProcessState(final String processState)
    {
        this.processState = processState;
    }

    public String getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(final String createdDate)
    {
        this.createdDate = createdDate;
    }

    public String getModifiedDate()
    {
        return modifiedDate;
    }

    public void setModifiedDate(final String modifiedDate)
    {
        this.modifiedDate = modifiedDate;
    }

    public ProcessStatisticsPTO getProcessStatistics()
    {
        return processStatistics;
    }

    public void setProcessStatistics(final ProcessStatisticsPTO processStatistics)
    {
        this.processStatistics = processStatistics;
    }
}
