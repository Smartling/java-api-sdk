package com.smartling.api.v2.client.auth;

import com.smartling.api.client.context.HttpClientSettings;
import com.smartling.api.v2.authentication.AuthenticationApi;
import com.smartling.api.v2.authentication.AuthenticationApiFactory;
import com.smartling.api.v2.authentication.pto.Authentication;
import com.smartling.api.v2.authentication.pto.AuthenticationRefreshRequest;
import com.smartling.api.v2.authentication.pto.AuthenticationRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Authenticates a given user identifier and user secret against Smartling's authentication service, returning a
 * valid OAuth 2 Bearer access token. This authenticator caches the result of calls to {@link #getAccessToken()}
 * and returns a reusable access token on subsequent calls until the access token is about to expire. If the access
 * token is expiring in less than 90 seconds, the access token is automatically refreshed before being returned to the
 * caller.
 *
 * @author Scott Rossillo
 */
@Slf4j
public class Authenticator
{
    static final int REFRESH_BEFORE_EXPIRES_MS = 90 * 1000;
    private final Clock clock;

    private final String userIdentifier;
    private final String userSecret;

    private Authentication authentication;
    private AuthenticationApi api;

    private volatile long expiresAt = -1;
    private volatile long refreshExpiresAt = -1;

    public Authenticator(String userIdentifier, String userSecret)
    {
        this(userIdentifier, userSecret, "https://api.smartling.com");
    }

    public Authenticator(String userIdentifier, String userSecret, String hostAndProtocol)
    {
        this(userIdentifier, userSecret, hostAndProtocol, new HttpClientSettings());
    }

    public Authenticator(String userIdentifier, String userSecret, String hostAndProtocol, HttpClientSettings httpClientSettings)
    {
        this(userIdentifier, userSecret, hostAndProtocol, httpClientSettings, new AuthenticationApiFactory().buildApi(hostAndProtocol));
    }

    Authenticator(String userIdentifier, String userSecret, String hostAndProtocol, HttpClientSettings httpClientSettings, AuthenticationApi api)
    {
        this(userIdentifier, userSecret, hostAndProtocol, httpClientSettings, api, new SystemClock());
    }

    Authenticator(String userIdentifier, String userSecret, String hostAndProtocol, HttpClientSettings httpClientSettings, AuthenticationApi api, Clock clock)
    {
        Objects.requireNonNull(userIdentifier, "userIdentifierRequired");
        Objects.requireNonNull(userSecret, "userSecret required");
        Objects.requireNonNull(api, "authentication API required");
        Objects.requireNonNull(clock, "clock required");
        this.userIdentifier = userIdentifier;
        this.userSecret = userSecret;
        this.api = api;
        this.clock = clock;
    }

    /**
     * Returns a valid access token for this authenticator's user identifier and secret.
     *
     * @return a valid access token
     */
    public String getAccessToken()
    {
        if (authentication != null && isValid())
        {
            log.debug("current token valid");
            return authentication.getAccessToken();
        }

        return refreshOrRequestNewAccessToken(false);
    }

    synchronized String refreshOrRequestNewAccessToken(boolean forceRefresh)
    {
        if (!forceRefresh && authentication != null && isValid())
        {
            log.debug("using current token");
            return authentication.getAccessToken();
        }
        if (authentication != null && isRefreshable())
        {
            log.debug("Going to refresh access token.");
            try
            {
                return refreshAccessToken();
            }
            catch (Exception e)
            {
                log.info("Failed to refresh accessToken. Requesting new token.");
            }
        }

        log.debug("Requesting new token.");
        return this.getAccessTokenInternal();
    }

    boolean isValid()
    {
        if (authentication == null)
            return false;

        return expiresAt > clock.currentTimeMillis() + REFRESH_BEFORE_EXPIRES_MS;
    }

    boolean isRefreshable()
    {
        if (authentication == null)
            return false;

        return refreshExpiresAt > clock.currentTimeMillis();
    }

    private synchronized String getAccessTokenInternal()
    {
        this.authentication = api.authenticate(new AuthenticationRequest(userIdentifier, userSecret));
        this.expiresAt = authentication.getExpiresIn() * 1000 + System.currentTimeMillis();
        this.refreshExpiresAt = authentication.getRefreshExpiresIn() * 100 + System.currentTimeMillis();
        return authentication.getAccessToken();
    }

    private synchronized String refreshAccessToken()
    {
        this.authentication = api.refresh(new AuthenticationRefreshRequest(authentication.getRefreshToken()));
        this.expiresAt = authentication.getExpiresIn() * 1000 + System.currentTimeMillis();
        this.refreshExpiresAt = authentication.getRefreshExpiresIn() * 100 + System.currentTimeMillis();
        return authentication.getAccessToken();
    }
}
