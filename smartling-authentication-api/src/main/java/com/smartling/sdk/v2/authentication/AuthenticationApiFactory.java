package com.smartling.sdk.v2.authentication;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.ClientRequestFilter;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public final class AuthenticationApiFactory
{
    private static final String DEFAULT_API_HOST_AND_PROTOCOL = "https://api.smartling.com";
    public AuthenticationApi createAuthenticationApi()
    {
       return this.createAuthenticationApi(DEFAULT_API_HOST_AND_PROTOCOL);
    }

    public AuthenticationApi createAuthenticationApi(String hostAndProtocol)
    {
        Objects.requireNonNull(hostAndProtocol, "hostAndProtocol required");
        final List<ClientRequestFilter> filters = new LinkedList<>();
        filters.add(new NoOpClientRequestFilter());

        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(DEFAULT_API_HOST_AND_PROTOCOL);
        return  target.proxy(AuthenticationApi.class);

    }

}
