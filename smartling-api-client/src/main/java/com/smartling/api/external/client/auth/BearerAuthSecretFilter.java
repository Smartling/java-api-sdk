package com.smartling.api.external.client.auth;

import com.smartling.api.client.authentication.Authenticator;
import com.smartling.api.external.client.HttpClientConfiguration;
import com.smartling.api.v2.HttpClientSettings;

public class BearerAuthSecretFilter extends AbstractBearerAuthFilter
{
    private final Authenticator authenticator;

    public BearerAuthSecretFilter(final String userIdentifier, final String userSecret)
    {
        authenticator = new Authenticator(userIdentifier, userSecret);
    }

    public BearerAuthSecretFilter(final String userIdentifier, final String userSecret, final String apiHost)
    {
        authenticator = new Authenticator(userIdentifier, userSecret, apiHost);
    }

    public BearerAuthSecretFilter(final String userIdentifier, final String userSecret, final String apiHost, HttpClientConfiguration httpClientConfiguration)
    {
        HttpClientSettings authenticatorHttpSettings = new HttpClientSettings();
        authenticatorHttpSettings.setProxy(HttpClientSettings.Proxy.newProxy(
                httpClientConfiguration.getProxyHost(),
                httpClientConfiguration.getProxyPort(),
                httpClientConfiguration.getProxyUser(),
                httpClientConfiguration.getProxyPassword()
        ));

        this.authenticator = new Authenticator(userIdentifier, userSecret, apiHost, authenticatorHttpSettings);
    }

    @Override
    public String getTokenString()
    {
        return authenticator.getAccessToken();
    }
}
