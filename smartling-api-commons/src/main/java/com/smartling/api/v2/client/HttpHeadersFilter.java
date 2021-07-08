package com.smartling.api.v2.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

public class HttpHeadersFilter implements ClientRequestFilter
{
    private Map<String, String> httpHeaders;

    public HttpHeadersFilter(Map<String, String> httpHeaders)
    {
        this.httpHeaders = httpHeaders;
    }

    public HttpHeadersFilter(String key, String value)
    {
        this.httpHeaders = new HashMap<>();
        this.httpHeaders.put(key, value);
    }

    @Override
    public void filter(ClientRequestContext clientRequestContext) throws IOException
    {
        for (Map.Entry<String, String> header : httpHeaders.entrySet())
        {
            clientRequestContext.getHeaders().add(header.getKey(), header.getValue());
        }
    }
}
