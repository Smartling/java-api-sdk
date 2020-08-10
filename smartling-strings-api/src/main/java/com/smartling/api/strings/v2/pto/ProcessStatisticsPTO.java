package com.smartling.api.strings.v2.pto;

import java.io.Serializable;

public class ProcessStatisticsPTO implements Serializable
{
    private int requested;
    private int processed;
    private int errored;
    private int skipped;

    public int getRequested()
    {
        return requested;
    }

    public void setRequested(int requested)
    {
        this.requested = requested;
    }

    public int getProcessed()
    {
        return processed;
    }

    public void setProcessed(int processed)
    {
        this.processed = processed;
    }

    public int getErrored()
    {
        return errored;
    }

    public void setErrored(int errored)
    {
        this.errored = errored;
    }

    public int getSkipped()
    {
        return skipped;
    }

    public void setSkipped(int skipped)
    {
        this.skipped = skipped;
    }
}
