package com.smartling.api.v2.client.auth;

import javax.ws.rs.client.ClientRequestFilter;

/**
 * Client request filter that injects an authorization header into
 * before the client request is dispatched.
 */
public interface AuthorizationRequestFilter extends ClientRequestFilter
{
    /**
     * Returns the authorization token used to authenticate client requests.
     *
     * @return the authorization token
     */
    String getTokenString();
}
