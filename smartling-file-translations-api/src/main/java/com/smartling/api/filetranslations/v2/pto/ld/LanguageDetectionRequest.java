package com.smartling.api.filetranslations.v2.pto.ld;

import com.smartling.api.filetranslations.v2.pto.Callback;

public class LanguageDetectionRequest
{
    private Callback callback;

    public LanguageDetectionRequest()
    {
    }

    public LanguageDetectionRequest(Callback callback)
    {
        this.callback = callback;
    }

    public Callback getCallback()
    {
        return callback;
    }

    public void setCallback(Callback callback)
    {
        this.callback = callback;
    }

    @Override
    public String toString()
    {
        return "LanguageDetectionRequest{" +
            "callback=" + callback +
            '}';
    }
}
