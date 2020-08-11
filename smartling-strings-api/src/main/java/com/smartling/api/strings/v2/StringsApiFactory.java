package com.smartling.api.strings.v2;

import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientFactory;

public class StringsApiFactory extends AbstractApiFactory<StringsApi>
{
    public  StringsApiFactory()
    {
        super();
    }

    public StringsApiFactory(ClientFactory clientFactory)
    {
        super(clientFactory);
    }

    @Override
    protected Class<StringsApi> getApiClass()
    {
        return StringsApi.class;
    }
}
