package com.smartling.api.sdk;

import com.smartling.api.v2.issues.IssuesApi;
import com.smartling.api.v2.issues.IssuesApiFactory;
import com.smartling.api.v2.locales.LocalesApi;
import com.smartling.api.v2.locales.LocalesApiFactory;

final class SmartlingApiImpl implements SmartlingApi
{
    private final IssuesApi issuesApi;
    private final LocalesApi localesApi;

    SmartlingApiImpl(String userIdentifier, String userSecret)
    {
        // FIXME: use a shared bearer filter
        issuesApi = new IssuesApiFactory().buildApi(userIdentifier, userSecret);
        localesApi = new LocalesApiFactory().buildApi(userIdentifier, userSecret);
    }

    @Override
    public IssuesApi issuesApi()
    {
        return issuesApi;
    }

    @Override
    public LocalesApi localesApi()
    {
        return localesApi;
    }
}
