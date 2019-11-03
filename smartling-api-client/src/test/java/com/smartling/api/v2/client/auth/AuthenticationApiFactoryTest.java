package com.smartling.api.v2.client.auth;

import com.smartling.api.sdk.v2.response.authentication.AuthenticationApi;
import com.smartling.api.sdk.v2.response.authentication.AuthenticationApiFactory;
import com.smartling.api.sdk.v2.response.authentication.pto.Authentication;
import com.smartling.api.sdk.v2.response.authentication.pto.AuthenticationRefreshRequest;
import com.smartling.api.sdk.v2.response.authentication.pto.AuthenticationRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuthenticationApiFactoryTest
{
    private final String userId = "hgkmuaxafqapfvciqnwvtvccsslcvt";
    private final String userSecret = "89kpakjd2gv25cqkr4u7tdhi3hIe.kteshei7r0m61d3roj01kd535k";

    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void createAuthenticationApi()
    {
        AuthenticationApiFactory factory = new AuthenticationApiFactory();
        AuthenticationApi api = factory.createAuthenticationApi();

        Authentication auth = api.authenticate(new AuthenticationRequest(userId, userSecret));
        System.err.println(auth);

        Authentication refreshedAuth = api.refresh(new AuthenticationRefreshRequest(auth.getRefreshToken()));
        System.err.println(refreshedAuth);
    }
}
