package com.smartling.api.locales.v2;

import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientFactory;

public class LocalesApiFactory extends AbstractApiFactory<LocalesApi>
{
    public LocalesApiFactory()
    {
        super();
    }

    public LocalesApiFactory(ClientFactory clientFactory)
    {
        super(clientFactory);
    }

    @Override
    protected Class<LocalesApi> getApiClass()
    {
        return LocalesApi.class;
    }
}
