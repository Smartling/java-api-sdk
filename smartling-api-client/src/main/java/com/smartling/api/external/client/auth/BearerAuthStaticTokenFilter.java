package com.smartling.api.external.client.auth;


public class BearerAuthStaticTokenFilter extends AbstractBearerAuthFilter
{
    private final String tokenString;

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
