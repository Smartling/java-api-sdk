package com.smartling.api.v2.authentication;

import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientFactory;

/**
 * Provides an authentication API factory.
 */
public final class AuthenticationApiFactory
{
    private final AuthApiFactory authApiFactory;

    /**
     * Creates a new authentication API factory using the given
     * client factory to create API proxies.
     *
     * @param clientFactory the <code>ClientFactory</code> to use when generating
     *                      API proxies (required)
     */
    public AuthenticationApiFactory(ClientFactory clientFactory)
    {
        this.authApiFactory = new AuthApiFactory(clientFactory);
    }

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
        AuthApiFactory(ClientFactory clientFactory)
        {
            super(clientFactory);
        }

        @Override
        protected Class<AuthenticationApi> getApiClass()
        {
            return AuthenticationApi.class;
        }
    }
}
