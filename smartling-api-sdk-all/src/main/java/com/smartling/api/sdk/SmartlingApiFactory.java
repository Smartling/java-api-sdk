package com.smartling.api.sdk;

import com.smartling.api.v2.client.ClientFactory;

import java.util.Objects;

/**
 * Provides a factory to create an instance of the Smartling API.
 */
public class SmartlingApiFactory
{
    /**
     * Returns a new Smartling API instance.
     *
     * @param userIdentifier your API v2 user identifier (required)
     * @param userSecret your API v2 user secret (required)
     *
     * @return a configured {@link SmartlingApi} instance
     */
    public SmartlingApi build(String userIdentifier, String userSecret)
    {
        return build(userIdentifier, userSecret, new ClientFactory());
    }

    /**
     * Returns a new Smartling API instance.
     *
     * @param userIdentifier your API v2 user identifier (required)
     * @param userSecret your API v2 user secret (required)
     * @param clientFactory the <code>ClientFactory</code> to use when constructing the API (required)
     *
     * @return a configured {@link SmartlingApi} instance
     */
    public SmartlingApi build(String userIdentifier, String userSecret, ClientFactory clientFactory)
    {
        Objects.requireNonNull(userIdentifier, "userIdentifer required");
        Objects.requireNonNull(userSecret, "userSecret required");
        Objects.requireNonNull(clientFactory, "clientFactory required");
        return new SmartlingApiImpl(userIdentifier, userSecret, clientFactory);
    }
}
