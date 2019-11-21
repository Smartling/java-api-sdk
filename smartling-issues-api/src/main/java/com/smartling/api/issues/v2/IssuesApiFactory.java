package com.smartling.api.issues.v2;

import com.smartling.api.v2.client.AbstractApiFactory;

public class IssuesApiFactory  extends AbstractApiFactory<IssuesApi>
{
    @Override
    protected Class<IssuesApi> getApiClass()
    {
        return IssuesApi.class;
    }
}
