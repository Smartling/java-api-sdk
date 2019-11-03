package com.smartling.api.v2.authentication;

import com.smartling.api.v2.client.AbstractApiFactory;

import javax.ws.rs.client.ClientRequestFilter;
import java.util.ArrayList;
import java.util.List;

public final class AuthenticationApiFactory
{
    private ApiFactory apiFactory = new ApiFactory();

    public AuthenticationApi buildApi()
    {
        return apiFactory.buildApiInternal();
    }

    public AuthenticationApi buildApi(String protocolAndHost)
    {
        return apiFactory.buildApiInternal(protocolAndHost);
    }

    private static class ApiFactory extends AbstractApiFactory<AuthenticationApi>
    {
        AuthenticationApi buildApiInternal()
        {
           return this.buildApiInternal(DEFAULT_API_HOST_AND_PROTOCOL);
        }

        AuthenticationApi buildApiInternal(String hostAndProtocol)
        {
            List<ClientRequestFilter> filters = new ArrayList<>();
            filters.add(new NoOpClientRequestFilter());
            return this.buildApi(filters, hostAndProtocol);
        }

        @Override
        protected Class<AuthenticationApi> getApiClass()
        {
            return AuthenticationApi.class;
        }
    }
}
