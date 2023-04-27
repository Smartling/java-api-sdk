package com.smartling.api.accounts.v2;

import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientFactory;

public class AccountsApiFactory extends AbstractApiFactory<AccountsApi>
{
    public AccountsApiFactory()
    {
        super();
    }

    public AccountsApiFactory(ClientFactory clientFactory)
    {
        super(clientFactory);
    }

    @Override
    protected Class<AccountsApi> getApiClass()
    {
        return AccountsApi.class;
    }
}
