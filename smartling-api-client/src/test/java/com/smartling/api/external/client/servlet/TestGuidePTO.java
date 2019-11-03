package com.smartling.api.external.client.servlet;

import com.smartling.sdk.v2.ResponseData;

public class TestGuidePTO implements ResponseData
{
    private String localeId;
    private String styleGuideUid;

    public String getLocaleId()
    {
        return localeId;
    }

    public void setLocaleId(final String localeId)
    {
        this.localeId = localeId;
    }

    public String getStyleGuideUid()
    {
        return styleGuideUid;
    }

    public void setStyleGuideUid(final String styleGuideUid)
    {
        this.styleGuideUid = styleGuideUid;
    }
}
