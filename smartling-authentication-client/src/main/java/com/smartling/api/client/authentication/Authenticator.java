package com.smartling.api.client.authentication;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.io.IOException;
import java.util.logging.Level;
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
    private static final String AUTH_URL_PATTERN = "https://%s/auth-api/v2/authenticate";
    private static final String REFRESH_URL_PATTERN = "https://%s/auth-api/v2/authenticate/refresh";
    private static final String DEFAULT_API_HOST = "api.smartling.com";
    private static Logger logger = Logger.getLogger(Authenticator.class.getName());

    private final HttpClient client;
    private final String userIdentifier;
    private final String userSecret;

    private String authUrl;
    private String refreshUrl;
    private Authentication authentication;

    public Authenticator(String userIdentifier, String userSecret)
    {
        this(userIdentifier, userSecret, DEFAULT_API_HOST);
    }

    public Authenticator(String userIdentifier, String userSecret, HttpClientSettings httpClientSettings)
    {
        this(userIdentifier, userSecret, DEFAULT_API_HOST, httpClientSettings);
    }

    public Authenticator(String userIdentifier, String userSecret, String apiHost)
    {
        this(userIdentifier, userSecret, apiHost, new HttpClientSettings());
    }

    public Authenticator(String userIdentifier, String userSecret, String apiHost, HttpClientSettings httpClientSettings)
    {
        this.userIdentifier = userIdentifier;
        this.userSecret = userSecret;
        this.authUrl = String.format(AUTH_URL_PATTERN, apiHost);
        this.refreshUrl = String.format(REFRESH_URL_PATTERN, apiHost);
        this.client = new HttpClient(httpClientSettings);
    }

    /**
     * Returns a valid access token for this authenticator's user identifier and secret.
     *
     * @return a valid access token
     * @throws AuthenticationException if an access couldn't be obtained for the user and secret
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
     * @throws AuthenticationException if an access couldn't be obtained for the user and secret
     */
    public String getAccessToken(String requestId)
    {
        if (authentication != null && authentication.isValid())
        {
            logger.finest("current token valid");
            return authentication.getAccessToken();
        }

        return refreshOrRequestNewAccessToken(requestId, false);
    }

    private synchronized String refreshOrRequestNewAccessToken(String requestId, boolean forceRefresh)
    {
        if (!forceRefresh && authentication != null && authentication.isValid())
        {
            logger.finest("current token valid");
            return authentication.getAccessToken();
        }
        if (authentication != null && authentication.isRefreshable())
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

        try
        {
            logger.info("Requesting new token.");
            return this.getAccessTokenInternal(requestId);
        }
        catch (IOException e)
        {
            throw new AuthenticationConnectionException("Unable to authenticate", e);
        }
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

    private JsonValue buildAuthRequest()
    {
        JsonObject request = new JsonObject();

        request.add("userIdentifier", userIdentifier);
        request.add("userSecret", userSecret);

        return request;
    }

    private JsonValue buildRefreshRequest()
    {
        JsonObject request = new JsonObject();

        request.add("refreshToken", authentication.getRefreshToken());

        return request;
    }

    private Authentication buildAuthentication(JsonObject data)
    {
        return AuthenticationBuilder.builder()
                .accessToken(data.getString("accessToken", null))
                .refreshToken(data.getString("refreshToken", null))
                .expiresIn(data.getInt("expiresIn", -1))
                .refreshExpiresIn(data.getInt("refreshExpiresIn", -1))
                .tokenType(data.getString("tokenType", null))
                .build();
    }

    private String getAccessTokenInternal(String requestId) throws IOException
    {
        return getAccessTokenFromResponse(client.post(authUrl, buildAuthRequest(), requestId));
    }

    private String refreshAccessToken(String requestId) throws IOException
    {
        return getAccessTokenFromResponse(client.post(refreshUrl, buildRefreshRequest(), requestId));
    }

    private String getAccessTokenFromResponse(ResponseEntity<JsonObject> response)
    {
        if (logger.isLoggable(Level.FINE))
            logger.fine(String.valueOf(response.getBody()));

        if (response.getStatus() == 401 || response.getStatus() == 403)
            throw new AccessDeniedException(response.getMessage());
        if (response.getStatus() == 400)
            throw new BadRequestException("Bad authentication request");
        else if(response.getStatus() != 200)
            throw new AuthenticationConnectionException("Bad response status: " + response.getStatus());

        this.authentication = buildAuthentication(response.getBody().get("response").asObject().get("data").asObject());

        return authentication.getAccessToken();
    }
}
