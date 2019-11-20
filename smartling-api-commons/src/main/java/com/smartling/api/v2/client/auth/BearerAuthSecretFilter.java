package com.smartling.api.v2.client.auth;

import com.smartling.api.v2.authentication.AuthenticationApi;
import com.smartling.api.v2.client.HttpClientConfiguration;
import com.smartling.api.client.context.HttpClientSettings;

import java.net.URL;
import java.util.Objects;

public class BearerAuthSecretFilter extends AbstractBearerAuthFilter
{
    private final Authenticator authenticator;

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
