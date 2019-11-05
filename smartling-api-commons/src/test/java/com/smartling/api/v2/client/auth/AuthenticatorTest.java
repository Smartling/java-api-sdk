package com.smartling.api.v2.client.auth;

import com.smartling.api.client.context.HttpClientSettings;
import com.smartling.api.v2.authentication.AuthenticationApi;
import com.smartling.api.v2.authentication.pto.Authentication;
import com.smartling.api.v2.authentication.pto.AuthenticationRefreshRequest;
import com.smartling.api.v2.authentication.pto.AuthenticationRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.smartling.api.v2.client.auth.Authenticator.REFRESH_BEFORE_EXPIRES_MS;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AuthenticatorTest
{
    private static final String USER_IDENTIFIER = "userIdentifier";
    private static final String USER_SECRET = "userSecret";
    private static final int ACCESS_TOKEN_TTL = 5 * 60;
    private static final int REFRESH_TOKEN_TTL = 10 * 60;

    private Authenticator authenticator;

    private Authentication auth;

    @Mock
    private AuthenticationApi authenticationApi;

    @Mock
    private Clock clock;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        authenticator = new Authenticator(
            USER_IDENTIFIER,
            USER_SECRET,
            "https://api.smartling.com",
            new HttpClientSettings(),
            authenticationApi,
            clock
        );

        auth = new Authentication(
            "accessToken",
            "refreshToken",
            ACCESS_TOKEN_TTL,
            REFRESH_TOKEN_TTL,
            "bearer"
        );

        when(authenticationApi.authenticate(any(AuthenticationRequest.class))).thenReturn(auth);
    }

    @Test
    public void getAccessToken()
    {
        assertEquals(auth.getAccessToken(), authenticator.getAccessToken());
        verify(authenticationApi).authenticate(eq(new AuthenticationRequest(USER_IDENTIFIER, USER_SECRET)));
    }

    @Test
    public void refreshOrRequestNewAccessTokenForce()
    {
        when(clock.currentTimeMillis()).thenReturn(System.currentTimeMillis());
        authenticator.getAccessToken();
        authenticator.refreshOrRequestNewAccessToken(true);
        verify(authenticationApi).refresh(any(AuthenticationRefreshRequest.class));
    }

    @Test
    public void refreshOrRequestNewAccessTokenNoForce()
    {
        when(clock.currentTimeMillis()).thenReturn(System.currentTimeMillis());
        authenticator.getAccessToken();
        authenticator.refreshOrRequestNewAccessToken(false);
        verify(authenticationApi, times(0)).refresh(any(AuthenticationRefreshRequest.class));
    }

    @Test
    public void refreshOrRequestNewAccessTokenNoTokens()
    {
        when(clock.currentTimeMillis()).thenReturn(System.currentTimeMillis());
        authenticator.getAccessToken();
        authenticator.refreshOrRequestNewAccessToken(false);
        verify(authenticationApi).authenticate(any(AuthenticationRequest.class));
        verify(authenticationApi, times(0)).refresh(any(AuthenticationRefreshRequest.class));
    }

    @Test
    public void isValidNotAccessed()
    {
        assertFalse(authenticator.isValid());
    }

    @Test
    public void isValidJustCreated()
    {
        when(clock.currentTimeMillis()).thenReturn(System.currentTimeMillis());
        authenticator.getAccessToken();
        assertTrue(authenticator.isValid());
        verify(authenticationApi, times(0)).refresh(any(AuthenticationRefreshRequest.class));
    }

    @Test
    public void isValidExpired()
    {
        authenticator.getAccessToken();
        when(clock.currentTimeMillis()).thenReturn(ACCESS_TOKEN_TTL * 1000 + System.currentTimeMillis());
        assertFalse(authenticator.isValid());
    }

    @Test
    public void isValidExpiring()
    {
        authenticator.getAccessToken();
        when(clock.currentTimeMillis()).thenReturn(ACCESS_TOKEN_TTL * 1000 + System.currentTimeMillis() - (REFRESH_BEFORE_EXPIRES_MS / 2));
        assertFalse(authenticator.isValid());
    }

    @Test
    public void isRefreshableNotAccessed()
    {
        assertFalse(authenticator.isRefreshable());
    }

    @Test
    public void isRefreshableJustCreated()
    {
        authenticator.getAccessToken();
        when(clock.currentTimeMillis()).thenReturn(System.currentTimeMillis());
        assertTrue(authenticator.isRefreshable());
    }

    @Test
    public void isRefreshableExpired()
    {
        authenticator.getAccessToken();
        when(clock.currentTimeMillis()).thenReturn(REFRESH_TOKEN_TTL * 1000 + System.currentTimeMillis());
        assertFalse(authenticator.isRefreshable());
    }
}
