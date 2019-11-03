package com.smartling.api.v2.client.auth;


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
