package com.smartling.api.v2.authentication;

import com.smartling.api.v2.client.AbstractApiFactory;

/**
 * Provides an authentication API factory.
 */
public final class AuthenticationApiFactory
{
    private AuthApiFactory authApiFactory = new AuthApiFactory();

    /**
     * Returns an authentication API proxy.
     *
     * @return a configured {@link AuthenticationApi} JAX-RS proxy
     */
    public AuthenticationApi buildApi()
    {
        return authApiFactory.buildApi(new NoOpAuthorizationFilter());
    }

    private static class AuthApiFactory extends AbstractApiFactory<AuthenticationApi>
    {
        @Override
        protected Class<AuthenticationApi> getApiClass()
        {
            return AuthenticationApi.class;
        }
    }
}
