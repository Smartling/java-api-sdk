package com.smartling.api.v2.client.auth;

import java.util.Objects;

/**
 * Provides an authorization filter that uses an authenticator
 * to generate bearer tokens for API requests.
 */
public class BearerAuthSecretFilter extends AbstractBearerAuthFilter
{
    private final Authenticator authenticator;

    /**
     * Constructs a new bearer authentication secret filter.
     *
     * @param authenticator the <code>Authenticator</code> to use for generating
     *                      bearer tokens (required)
     */
    public BearerAuthSecretFilter(Authenticator authenticator)
    {
        Objects.requireNonNull(authenticator, "authenticator required");
        this.authenticator = authenticator;
    }

    @Override
    public String getTokenString()
    {
        return authenticator.getAccessToken();
    }
}
