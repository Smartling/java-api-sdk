package com.smartling.api.sdk;

import com.smartling.api.v2.client.AbstractApiFactory;

public class SmartlingApiFactory extends AbstractApiFactory<SmartlingApi>
{
    @Override
    protected Class<SmartlingApi> getApiClass()
    {
        return SmartlingApi.class;
    }
}
