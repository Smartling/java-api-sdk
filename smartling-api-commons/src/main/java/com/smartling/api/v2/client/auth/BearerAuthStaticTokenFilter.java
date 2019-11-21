package com.smartling.api.v2.client.auth;

/**
 * Provides an authorization filter that uses a static token value
 * to authorize API calls. This class is mostly useful for testing.
 */
public class BearerAuthStaticTokenFilter extends AbstractBearerAuthFilter
{
    private final String tokenString;

    /**
     * Constructs a new bearer static token filter using the given
     * token string.
     *
     * @param tokenString the token string to use for API authorization
     */
    public BearerAuthStaticTokenFilter(String tokenString)
    {
        this.tokenString = tokenString;
    }

    @Override
    public String getTokenString()
    {
        return tokenString;
    }
}
