package com.smartling.api.jobs.v3;

import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientFactory;

public class TranslationJobsApiFactory extends AbstractApiFactory<TranslationJobsApi>
{
    public TranslationJobsApiFactory()
    {
        super();
    }

    public TranslationJobsApiFactory(ClientFactory clientFactory)
    {
        super(clientFactory);
    }

    @Override
    protected Class<TranslationJobsApi> getApiClass()
    {
        return TranslationJobsApi.class;
    }
}
