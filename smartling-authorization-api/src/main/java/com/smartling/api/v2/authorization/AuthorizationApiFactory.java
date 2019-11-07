package com.smartling.api.v2.authorization;

import com.smartling.api.v2.client.AbstractApiFactory;

/**
 * Provides an API factory for the Smartling authorization API.
 */
public class AuthorizationApiFactory extends AbstractApiFactory<AuthorizationApi>
{
    @Override
    protected Class<AuthorizationApi> getApiClass()
    {
        return AuthorizationApi.class;
    }
}
