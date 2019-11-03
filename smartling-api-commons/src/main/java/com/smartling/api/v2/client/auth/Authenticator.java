package com.smartling.api.v2.client.auth;

import com.smartling.api.client.context.HttpClientSettings;
import com.smartling.api.v2.authentication.AuthenticationApi;
import com.smartling.api.v2.authentication.AuthenticationApiFactory;
import com.smartling.api.v2.authentication.pto.Authentication;
import com.smartling.api.v2.authentication.pto.AuthenticationRefreshRequest;
import com.smartling.api.v2.authentication.pto.AuthenticationRequest;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Authenticates a given user identifier and user secret against Smartling's authentication service, returning a
 * valid OAuth 2 Bearer access token. This authenticator caches the result of calls to {@link #getAccessToken()}
 * and returns a reusable access token on subsequent calls until the access token is about to expire. If the access
 * token is expiring in less than 90 seconds, the access token is automatically refreshed before being returned to the
 * caller.
 *
 * @author Scott Rossillo
 */
public class Authenticator
{
    private static Logger logger = Logger.getLogger(Authenticator.class.getName());

    private static final int REFRESH_BEFORE_EXPIRES_MS = 90 * 1000;
    private final Clock clock;

    private final String userIdentifier;
    private final String userSecret;

    private Authentication authentication;
    private AuthenticationApi api;

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
            logger.finest("current token valid");
            return authentication.getAccessToken();
        }

        return refreshOrRequestNewAccessToken(false);
    }

    private synchronized String refreshOrRequestNewAccessToken(boolean forceRefresh)
    {
        if (!forceRefresh && authentication != null && isValid())
        {
            logger.finest("current token valid");
        }
        if (authentication != null && isRefreshable())
        {
            logger.info("Going to refresh access token.");
            try
            {
                return refreshAccessToken();
            }
            catch (Exception e)
            {
                logger.warning("Failed to refresh accessToken. Requesting new token.");
            }
        }

        logger.info("Requesting new token.");
        return this.getAccessTokenInternal();
    }

    boolean isValid()
    {
        if (authentication == null)
            return false;

        //long expiryTime = authentication.getExpiresIn() * 1000 + clock.currentTimeMillis();

        //return authentication.getExpiresIn() * 1000 + clock.currentTimeMillis() > clock.currentTimeMillis() - REFRESH_BEFORE_EXPIRES_MS;
        return authentication.getExpiresIn() * 1000 + clock.currentTimeMillis() > clock.currentTimeMillis();
    }

    boolean isRefreshable()
    {
        if (authentication == null)
            return false;

        return authentication.getExpiresIn() * 1000 + clock.currentTimeMillis() > clock.currentTimeMillis();
    }

    private synchronized String getAccessTokenInternal()
    {
        this.authentication = api.authenticate(new AuthenticationRequest(userIdentifier, userSecret));
        return authentication.getAccessToken();
    }

    private synchronized String refreshAccessToken()
    {
        this.authentication = api.refresh(new AuthenticationRefreshRequest(authentication.getRefreshToken()));
        return authentication.getAccessToken();
    }
}
