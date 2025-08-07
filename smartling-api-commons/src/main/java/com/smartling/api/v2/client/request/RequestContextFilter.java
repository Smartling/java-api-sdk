package com.smartling.api.v2.client.request;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;

public class RequestContextFilter implements ClientRequestFilter
{
    @Override
    public void filter(ClientRequestContext clientRequestContext) throws IOException
    {
        RequestContextHolder.setContext(RequestContext.fromClientRequestContext(clientRequestContext));
    }
}
