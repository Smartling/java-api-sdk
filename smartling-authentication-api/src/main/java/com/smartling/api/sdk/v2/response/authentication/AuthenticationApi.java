package com.smartling.api.sdk.v2.response.authentication;

import com.smartling.api.sdk.v2.response.authentication.pto.Authentication;
import com.smartling.api.sdk.v2.response.authentication.pto.AuthenticationRefreshRequest;
import com.smartling.api.sdk.v2.response.authentication.pto.AuthenticationRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/auth-api/v2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AuthenticationApi
{
    /**
     * Returns an authentication that may be used for subsequent Smartling API
     * calls and a refresh token that may be used to refresh your authentication.
     *
     * @param authenticationRequest the authentication request
     * @return an {@link Authentication} containing an access token and refresh
     * token
     */
    @POST
    @Path("/authenticate")
    Authentication authenticate(AuthenticationRequest authenticationRequest);

    /**
     * Returns a fresh authentication given the refresh token from a previous
     * authentication request. Authentications may be refreshed for up to 24 hours
     * and are preferable to re-authenticating for every API request.o
     *
     * Smartling provides an authentication client that may be used instead of
     * calling the authentication API directly.
     *
     * @param refreshRequest the authentication refresh request
     * @return an {@link Authentication} containing an access token and refresh
     * token
     */
    @POST
    @Path("/authenticate/refresh")
    Authentication refresh(AuthenticationRefreshRequest refreshRequest);
}
