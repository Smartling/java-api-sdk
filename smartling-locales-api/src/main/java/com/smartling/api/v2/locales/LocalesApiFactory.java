package com.smartling.api.v2.locales;

import com.smartling.api.v2.client.AbstractApiFactory;

public class LocalesApiFactory extends AbstractApiFactory<LocalesApi>
{
    @Override
    protected Class<LocalesApi> getApiClass()
    {
        return LocalesApi.class;
    }
}
