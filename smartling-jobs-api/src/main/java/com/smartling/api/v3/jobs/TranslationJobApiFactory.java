package com.smartling.api.v3.jobs;

import com.smartling.api.v2.client.AbstractApiFactory;

public class TranslationJobApiFactory extends AbstractApiFactory<TranslationJobApi>
{
    @Override
    protected Class<TranslationJobApi> getApiClass()
    {
        return TranslationJobApi.class;
    }
}
