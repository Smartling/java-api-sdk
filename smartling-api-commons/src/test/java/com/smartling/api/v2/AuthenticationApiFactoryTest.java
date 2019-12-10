package com.smartling.api.v2;

import com.smartling.api.v2.authentication.AuthenticationApiFactory;
import com.smartling.api.v2.client.ClientFactory;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class AuthenticationApiFactoryTest
{
    @Test(expected = NullPointerException.class)
    public void createAuthenticationApiNoClientFactory()
    {
        new AuthenticationApiFactory(null);
    }

    @Test
    public void createAuthenticationApi()
    {
        AuthenticationApiFactory factory = new AuthenticationApiFactory(new ClientFactory());
        assertNotNull(factory.buildApi());
    }
}
