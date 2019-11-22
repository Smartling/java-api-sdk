package com.smartling.api.sdk;

import com.smartling.api.jobs.v3.TranslationJobsApi;
import com.smartling.api.jobs.v3.TranslationJobsApiFactory;
import com.smartling.api.issues.v2.IssuesApi;
import com.smartling.api.issues.v2.IssuesApiFactory;
import com.smartling.api.locales.v2.LocalesApi;
import com.smartling.api.locales.v2.LocalesApiFactory;

final class SmartlingApiImpl implements SmartlingApi
{
    private final IssuesApi issuesApi;
    private final LocalesApi localesApi;

    private final TranslationJobsApi translationJobsApi;

    SmartlingApiImpl(String userIdentifier, String userSecret)
    {
        // FIXME: use a shared bearer filter
        issuesApi = new IssuesApiFactory().buildApi(userIdentifier, userSecret);
        localesApi = new LocalesApiFactory().buildApi(userIdentifier, userSecret);
        translationJobsApi = new TranslationJobsApiFactory().buildApi(userIdentifier, userSecret);
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

    @Override
    public TranslationJobsApi translationJobsApi()
    {
        return translationJobsApi;
    }
}
