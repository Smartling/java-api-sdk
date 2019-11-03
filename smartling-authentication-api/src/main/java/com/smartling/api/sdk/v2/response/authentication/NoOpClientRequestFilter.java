package com.smartling.api.sdk.v2.response.authentication;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;

class NoOpClientRequestFilter implements ClientRequestFilter
{
    @Override
    public void filter(ClientRequestContext clientRequestContext) throws IOException
    {

    }
}
