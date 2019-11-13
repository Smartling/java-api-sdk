package com.smartling.api.sdk;

import com.smartling.api.v2.issues.IssuesApi;
import com.smartling.api.v2.issues.IssuesApiFactory;

final class SmartlingApiImpl implements SmartlingApi
{
    private final IssuesApi issuesApi;

    SmartlingApiImpl(String userIdentifier, String userSecret)
    {
        issuesApi = new IssuesApiFactory().buildApi(userIdentifier, userSecret);
    }

    @Override
    public IssuesApi issuesApi()
    {
        return issuesApi;
    }
}
