package com.smartling.api.contexts.v2;

import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientFactory;

public class ContextsApiFactory extends AbstractApiFactory<ContextsApi>
{
    public ContextsApiFactory()
    {
        super();
    }

    public ContextsApiFactory(ClientFactory clientFactory)
    {
        super(clientFactory);
    }

    @Override
    protected Class<ContextsApi> getApiClass()
    {
        return ContextsApi.class;
    }
}
