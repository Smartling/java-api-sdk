package com.smartling.sdk.v2.authentication;

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
