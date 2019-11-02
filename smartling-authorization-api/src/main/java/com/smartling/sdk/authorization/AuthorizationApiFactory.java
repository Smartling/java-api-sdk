package com.smartling.sdk.authorization;

import com.smartling.api.external.client.AbstractApiFactory;

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
