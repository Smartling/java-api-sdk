package com.smartling.api.issues.v2;

import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientFactory;

public class IssuesApiFactory extends AbstractApiFactory<IssuesApi>
{
    public IssuesApiFactory()
    {
        super();
    }

    public IssuesApiFactory(ClientFactory clientFactory)
    {
        super(clientFactory);
    }

    @Override
    protected Class<IssuesApi> getApiClass()
    {
        return IssuesApi.class;
    }
}
