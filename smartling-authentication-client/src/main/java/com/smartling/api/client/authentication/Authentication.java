package com.smartling.api.client.authentication;

import java.io.Serializable;

/**
 * Represents an OAuth 2 authentication.
 *
 * @author Scott Rossillo
 */
class Authentication implements Serializable
{
    private final int refreshBeforeExpires = 90 * 1000; // 90 sec

    private String accessToken;
    private String refreshToken;
    private int expiresIn;
    private int refreshExpiresIn;
    private long expiresAt;
    private long refreshExpiresAt;

    private String tokenType;

    Authentication(String accessToken, String refreshToken, int expiresIn, int refreshExpiresIn, String tokenType)
    {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.refreshExpiresIn = refreshExpiresIn;
        this.tokenType = tokenType;
        this.expiresAt = System.currentTimeMillis() + (expiresIn * 1000);
        this.refreshExpiresAt = System.currentTimeMillis() + (refreshExpiresIn * 1000);
    }

    String getAccessToken()
    {
        return accessToken;
    }

    String getRefreshToken()
    {
        return refreshToken;
    }

    int getExpiresIn()
    {
        return expiresIn;
    }

    int getRefreshExpiresIn()
    {
        return refreshExpiresIn;
    }

    String getTokenType()
    {
        return tokenType;
    }

    boolean isValid()
    {
        return expiresAt - System.currentTimeMillis() > refreshBeforeExpires;
    }

    boolean isRefreshable()
    {
        return refreshExpiresAt - System.currentTimeMillis() > 0;
    }

    @Override
    public String toString()
    {
        return "Authentication{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", refreshExpiresIn='" + refreshExpiresIn + '\'' +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }
}
