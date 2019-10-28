package com.smartling.api.client.authentication;

import com.smartling.api.client.authentication.HttpClientSettings.Proxy;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

/**
 * Authenticator tests.
 */
@Ignore
public class AuthenticatorTest
{
    private String userIdentifier;
    private String userSecret;
    private String apiHost;

    private Authenticator authenticator;

    @Before
    public void setUp() throws Exception
    {
        userIdentifier = System.getenv("API_USER_IDENTIFIER");
        userSecret = System.getenv("API_USER_SECRET");
        apiHost = System.getenv("API_HOST");

        authenticator = new Authenticator(userIdentifier, userSecret, apiHost);
    }

    @Test
    public void testGetAccessToken() throws Exception
    {
        System.err.println(authenticator.getAccessToken());
        assertNotNull(authenticator.getAccessToken());
        assertNotNull(authenticator.getAccessToken());
    }

    @Test(expected = AccessDeniedException.class)
    public void testGetAccessTokenUnauthorized() throws Exception
    {
        authenticator = new Authenticator("foo", "bar");
        authenticator.getAccessToken();
    }

    @Test(expected = BadRequestException.class)
    public void testGetAccessTokenIncomplete() throws Exception
    {
        authenticator = new Authenticator("foo", null);
        authenticator.getAccessToken();
    }

    @Test
    public void testGetAccessTokenHeader() throws Exception
    {
        System.err.println(authenticator.getAccessTokenHeader());
        assertNotNull(authenticator.getAccessTokenHeader());
        assertNotNull(authenticator.getAccessTokenHeader());
        assertTrue(authenticator.getAccessTokenHeader().startsWith("Bearer "));
    }

    @Test(expected = AccessDeniedException.class)
    public void testGetAccessTokenHeaderUnauthorized() throws Exception
    {
        authenticator = new Authenticator("foo", "bar");
        authenticator.getAccessTokenHeader();
    }

    @Test(expected = BadRequestException.class)
    public void testGetAccessTokenHeaderIncomplete() throws Exception
    {
        authenticator = new Authenticator("foo", null);
        authenticator.getAccessTokenHeader();
    }

    @Test(expected = AuthenticationConnectionException.class)
    public void testUsingAnonymousProxy() throws Exception
    {
        HttpClientSettings httpClientSettings = new HttpClientSettings();
        httpClientSettings.setProxy(Proxy.anonymous("127.0.0.1", 18765));
        Authenticator authenticator = new Authenticator(userIdentifier, userSecret, apiHost, httpClientSettings);

        authenticator.getAccessToken();
    }

    @Test(expected = AuthenticationConnectionException.class)
    public void testUsingProxyWithAuthentication() throws Exception
    {
        HttpClientSettings httpClientSettings = new HttpClientSettings();
        httpClientSettings.setProxy(Proxy.withAuthentication("127.0.0.1", 18765, "user", "secret"));
        Authenticator authenticator = new Authenticator(userIdentifier, userSecret, apiHost, httpClientSettings);

        authenticator.getAccessToken();
    }
}
