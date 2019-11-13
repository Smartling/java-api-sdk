package com.smartling.api.locales.v2;

import com.smartling.api.v2.client.AbstractApiFactory;

public class LocalesApiFactory extends AbstractApiFactory<LocalesApi>
{
    @Override
    protected Class<LocalesApi> getApiClass()
    {
        return LocalesApi.class;
    }
}
