package com.smartling.api.client.authentication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({System.class, Authentication.class})
public class AuthenticationTest
{
    private static final String ACCESS_TOKEN = "dsfahsdlfhsdf";
    private static final String REFRESH_TOKEN = "aaaaaaa";
    private static final int EXPIRES_IN = 480;
    private static final int REFRESH_EXPIRES_IN = 3600;
    private static final String TOKEN_TYPE = "Bearer";


    @Before
    public void setUp()
    {
        PowerMockito.mockStatic(System.class);
    }

    @Test
    public void testCreation()
    {
        final long currentTime = 11111111000L;
        PowerMockito.when(System.currentTimeMillis()).thenReturn(currentTime);
        Authentication authentication = new Authentication(ACCESS_TOKEN, REFRESH_TOKEN, EXPIRES_IN, REFRESH_EXPIRES_IN, TOKEN_TYPE);

        assertEquals(ACCESS_TOKEN, authentication.getAccessToken());
        assertEquals(REFRESH_TOKEN, authentication.getRefreshToken());
        assertEquals(TOKEN_TYPE, authentication.getTokenType());
        assertEquals(EXPIRES_IN, authentication.getExpiresIn());
        assertEquals(REFRESH_EXPIRES_IN, authentication.getRefreshExpiresIn());
        //checking token expiration time
        PowerMockito.when(System.currentTimeMillis()).thenReturn(currentTime + 481*1000L);
        assertFalse(authentication.isValid());
        PowerMockito.when(System.currentTimeMillis()).thenReturn(currentTime + 380*1000L);
        assertTrue(authentication.isValid());
        PowerMockito.when(System.currentTimeMillis()).thenReturn(currentTime + (EXPIRES_IN - 90)*1000L - 1L);
        assertTrue(authentication.isValid());
        PowerMockito.when(System.currentTimeMillis()).thenReturn(currentTime + (EXPIRES_IN - 90)*1000L + 1L);
        assertFalse(authentication.isValid());
        //checking refresh token expiration time
        PowerMockito.when(System.currentTimeMillis()).thenReturn(currentTime + REFRESH_EXPIRES_IN * 1000L - 1L);
        assertTrue(authentication.isRefreshable());
        PowerMockito.when(System.currentTimeMillis()).thenReturn(currentTime + REFRESH_EXPIRES_IN * 1000L + 1L);
        assertFalse(authentication.isRefreshable());
    }
}