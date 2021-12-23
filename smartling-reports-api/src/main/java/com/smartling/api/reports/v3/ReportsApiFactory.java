package com.smartling.api.reports.v3;

import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientFactory;

public class ReportsApiFactory extends AbstractApiFactory<ReportsApi>
{
    public ReportsApiFactory()
    {
        super();
    }

    public ReportsApiFactory(ClientFactory clientFactory)
    {
        super(clientFactory);
    }

    @Override
    protected Class<ReportsApi> getApiClass()
    {
        return ReportsApi.class;
    }
}
