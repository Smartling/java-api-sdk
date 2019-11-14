package com.smartling.api.jobs.v3;

import com.smartling.api.v2.client.AbstractApiFactory;

public class TranslationJobsApiFactory extends AbstractApiFactory<TranslationJobsApi>
{
    @Override
    protected Class<TranslationJobsApi> getApiClass()
    {
        return TranslationJobsApi.class;
    }
}
