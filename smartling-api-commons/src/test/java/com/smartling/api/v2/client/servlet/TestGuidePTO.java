package com.smartling.api.v2.client.servlet;

import com.smartling.api.v2.response.ResponseData;

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
