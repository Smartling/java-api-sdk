package com.smartling.api.v2.issues;

import com.smartling.api.v2.client.AbstractApiFactory;

public class IssuesApiFactory  extends AbstractApiFactory<IssuesApi>
{
    @Override
    protected Class<IssuesApi> getApiClass()
    {
        return IssuesApi.class;
    }
}
