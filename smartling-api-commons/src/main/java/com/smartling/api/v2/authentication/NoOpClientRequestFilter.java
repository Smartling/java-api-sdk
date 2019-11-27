package com.smartling.api.v2.authentication;

import com.smartling.api.v2.client.auth.AuthorizationRequestFilter;

import javax.ws.rs.client.ClientRequestContext;
import java.io.IOException;

final class NoOpAuthorizationFilter implements AuthorizationRequestFilter
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
