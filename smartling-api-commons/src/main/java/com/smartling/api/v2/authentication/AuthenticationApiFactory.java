package com.smartling.api.v2.authentication;

import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.auth.AuthorizationRequestFilter;

import javax.ws.rs.client.ClientRequestFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
        return authApiFactory.buildApi(new NoOpClientRequestFilter());
    }

    /**
     * Returns an authentication API proxy.
     *
     * @param baseUrl the base URL for the authentication API
     *
     * @return a configured {@link AuthenticationApi} JAX-RS proxy
     */
    /*
    public AuthenticationApi buildApi(URL baseUrl)
    {
        return authApiFactory.buildApi(baseUrl);
    }
     */

    private static class AuthApiFactory extends AbstractApiFactory<AuthenticationApi>
    {
        @Override
        protected Class<AuthenticationApi> getApiClass()
        {
            return AuthenticationApi.class;
        }
    }
}
