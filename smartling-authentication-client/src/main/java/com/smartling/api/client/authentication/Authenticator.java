package com.smartling.api.client.authentication;

import com.smartling.api.v2.HttpClientSettings;
import com.smartling.sdk.v2.authentication.AuthenticationApi;
import com.smartling.sdk.v2.authentication.AuthenticationApiFactory;
import com.smartling.sdk.v2.authentication.pto.Authentication;
import com.smartling.sdk.v2.authentication.pto.AuthenticationRefreshRequest;
import com.smartling.sdk.v2.authentication.pto.AuthenticationRequest;

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
        this(userIdentifier, userSecret, hostAndProtocol, httpClientSettings, new SystemClock());
    }

    Authenticator(String userIdentifier, String userSecret, String hostAndProtocol, HttpClientSettings httpClientSettings, Clock clock)
    {
        Objects.requireNonNull(userIdentifier, "userIdentifierRequired");
        Objects.requireNonNull(userSecret, "userSecret required");
        this.userIdentifier = userIdentifier;
        this.userSecret = userSecret;
        this.clock = new SystemClock();
        this.api = new AuthenticationApiFactory().createAuthenticationApi(hostAndProtocol);
    }

    /**
     * Returns a valid access token for this authenticator's user identifier and secret.
     *
     * @return a valid access token
     */
    public String getAccessToken()
    {
        return getAccessToken(null);
    }

    /**
     * Returns a valid access token for this authenticator's user identifier and secret.
     *
     * @param requestId an identifier assigned to all rest calls initiated by this method. It does not affect
     *                  method logic and is indented for internal debugging purposes only.
     * @return a valid access token
     */
    public String getAccessToken(String requestId)
    {
        if (authentication != null && isValid())
        {
            logger.finest("current token valid");
            return authentication.getAccessToken();
        }

        return refreshOrRequestNewAccessToken(requestId, false);
    }

    private synchronized String refreshOrRequestNewAccessToken(String requestId, boolean forceRefresh)
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
                return refreshAccessToken(requestId);
            }
            catch (Exception e)
            {
                logger.warning("Failed to refresh accessToken. Requesting new token.");
            }
        }

        logger.info("Requesting new token.");
        return this.getAccessTokenInternal(requestId);
    }

    /**
     * Returns a valid access token header (tokenType + accessToken) for this authenticator's user identifier and secret.
     *
     * @return a valid access token
     * @throws AuthenticationException if an access couldn't be obtained for the user and secret
     */
    public String getAccessTokenHeader()
    {
        final String accessToken = getAccessToken();
        return authentication.getTokenType() + ' ' + accessToken;
    }

    /**
     * Returns a valid access token header (tokenType + accessToken) for this authenticator's user identifier and secret.
     * Forces token refresh
     *
     * @return a valid access token
     * @throws AuthenticationException if an access couldn't be obtained for the user and secret
     */
    public String getAccessTokenHeaderForceRefresh()
    {
        final String accessToken = refreshOrRequestNewAccessToken(null, true);
        return authentication.getTokenType() + ' ' + accessToken;
    }

    boolean isValid()
    {
        if (authentication == null)
            return false;

        return authentication.getExpiresIn() + clock.currentTimeMillis() > clock.currentTimeMillis() + REFRESH_BEFORE_EXPIRES_MS;
    }

    boolean isRefreshable()
    {
        if (authentication == null)
            return false;

        return authentication.getExpiresIn() + clock.currentTimeMillis() > clock.currentTimeMillis();
    }

    private synchronized String getAccessTokenInternal(String requestId)
    {
        this.authentication = api.authenticate(new AuthenticationRequest(userIdentifier, userSecret));
        return authentication.getAccessToken();
    }

    private synchronized String refreshAccessToken(String requestId)
    {
        this.authentication = api.refresh(new AuthenticationRefreshRequest(authentication.getRefreshToken()));
        return authentication.getAccessToken();
    }
}
