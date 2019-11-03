package com.smartling.api.v2.client.auth;


import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;

public abstract class AbstractBearerAuthFilter implements ClientRequestFilter
{
    private static final String BEARER = "Bearer ";

    public abstract String getTokenString();

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException
    {
        final String tokenString = getTokenString();
        if (null != tokenString)
            requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, BEARER + tokenString);
    }
}
