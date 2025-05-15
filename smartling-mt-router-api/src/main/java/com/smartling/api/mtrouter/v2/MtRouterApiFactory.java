package com.smartling.api.mtrouter.v2;

import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientFactory;

public class MtRouterApiFactory extends AbstractApiFactory<MtRouterApi>
{
    public MtRouterApiFactory()
    {
        super();
    }

    public MtRouterApiFactory(ClientFactory clientFactory)
    {
        super(clientFactory);
    }

    @Override
    protected Class<MtRouterApi> getApiClass()
    {
        return MtRouterApi.class;
    }
}
