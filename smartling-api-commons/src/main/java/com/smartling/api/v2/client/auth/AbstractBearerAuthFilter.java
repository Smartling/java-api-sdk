package com.smartling.api.v2.client.auth;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;

/**
 * Authorization filter that injects an Oauth 2 bearer token into the
 * authorization header of a client request.
 */
public abstract class AbstractBearerAuthFilter implements AuthorizationRequestFilter
{
    private static final String BEARER = "Bearer ";

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException
    {
        final String tokenString = getTokenString();
        if (null != tokenString)
        {
            requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, BEARER + tokenString);
        }
    }
}
