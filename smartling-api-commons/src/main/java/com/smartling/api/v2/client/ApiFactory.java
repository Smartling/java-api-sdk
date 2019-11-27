package com.smartling.api.v2.client;

import com.smartling.api.v2.client.auth.AuthorizationRequestFilter;

/**
 * Factory to create JAX-RS proxies for Smartling APIs.
 *
 *  @param <T> the API interface type to proxy
 */
public interface ApiFactory<T>
{
    /**
     * Returns a proxied API authenticated with the given user identifier and secret.
     *
     * @param userIdentifier the API v2 user identifier for authentication (required)
     * @param userSecret the API v2 user secret for authentication (required)
     *
     * @return a proxied API of type <code>T</code>
     */
    T buildApi(final String userIdentifier, final String userSecret);

    /**
     * Returns a proxied API authenticated with the given authorization request filter.
     *
     * @param filter the <code>AuthorizationRequestFilter</code> to use for authentication
     *
     * @return a proxied API of type <code>T</code>
     */
    T buildApi(AuthorizationRequestFilter filter);

    /**
     * Returns a proxied API authenticated with the given authorization request filter
     * built with the given client configuration.
     *
     * @param filter the <code>AuthorizationRequestFilter</code> to use for authentication
     * @param clientConfiguration the <code>ClientConfiguration</code> to use when building
     *                            the proxied API
     *
     * @return a proxied API of type <code>T</code>
     */
    T buildApi(AuthorizationRequestFilter filter, ClientConfiguration clientConfiguration);
}
