package com.smartling.api.v2.client;

import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;
import java.util.UUID;

public class DefaultRequestIdFilter implements ClientRequestFilter
{
    private static final String REQUEST_ID_HEADER = "X-SL-RequestId";

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException
    {
        String requestId = requestContext.getHeaderString(REQUEST_ID_HEADER);
        if (StringUtils.isBlank(requestId))
        {
            requestContext.getHeaders().addFirst(REQUEST_ID_HEADER, UUID.randomUUID().toString());
        }
    }
}
