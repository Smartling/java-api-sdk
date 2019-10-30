package com.smartling.api.client.authentication;

/**
 * Created by scott on 9/24/15.
 */
class AuthenticationBuilder
{
    private String accessToken;
    private String refreshToken;
    private int expiresIn;
    private int refreshExpiresIn;
    private String tokenType;

    private AuthenticationBuilder()
    {

    }

    public AuthenticationBuilder accessToken(String accessToken)
    {
        this.accessToken = accessToken;
        return this;
    }

    public AuthenticationBuilder refreshToken(String refreshToken)
    {
        this.refreshToken = refreshToken;
        return this;
    }

    public AuthenticationBuilder expiresIn(int expiresIn)
    {
        this.expiresIn = expiresIn;
        return this;
    }

    public AuthenticationBuilder refreshExpiresIn(int refreshExpiresIn)
    {
        this.refreshExpiresIn = refreshExpiresIn;
        return this;
    }

    public AuthenticationBuilder tokenType(String tokenType)
    {
        this.tokenType = tokenType;
        return this;
    }

    public Authentication build()
    {
        Assert.notNull(accessToken, "accessToken required");
        Assert.notNull(refreshToken, "refreshToken required");
        Assert.notNegative(expiresIn, "expiresIn required");
        Assert.notNegative(refreshExpiresIn, "refreshExpiresIn required");
        Assert.notNull(tokenType, "tokenType required");

        return new Authentication(accessToken, refreshToken, expiresIn, refreshExpiresIn, tokenType);
    }

    static AuthenticationBuilder builder()
    {
        return new AuthenticationBuilder();
    }

}
