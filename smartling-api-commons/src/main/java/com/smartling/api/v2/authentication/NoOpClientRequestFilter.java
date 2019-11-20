package com.smartling.api.v2.authentication;

import com.smartling.api.v2.client.auth.AuthorizationRequestFilter;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;

final class NoOpClientRequestFilter implements AuthorizationRequestFilter
{
    @Override
    public void filter(ClientRequestContext clientRequestContext) throws IOException
    {

    }

    @Override
    public String getTokenString()
    {
        return null;
    }
}
