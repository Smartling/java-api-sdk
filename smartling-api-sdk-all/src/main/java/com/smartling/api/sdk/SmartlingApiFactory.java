package com.smartling.api.sdk;

import java.util.Objects;

/**
 * Provides a factory to create an instance of the Smartling API.
 */
public class SmartlingApiFactory
{
    /**
     * Returns a Smartling API instance
     *
     * @param userIdentifier your API v2 user identifier (required)
     * @param userSecret your API v2 user secret (required)
     *
     * @return a configured {@link SmartlingApi} instance
     */
    public SmartlingApi build(String userIdentifier, String userSecret)
    {
        Objects.requireNonNull(userIdentifier, "userIdentifer required");
        Objects.requireNonNull(userSecret, "userSecret required");
        return new SmartlingApiImpl(userIdentifier, userSecret);
    }
}
