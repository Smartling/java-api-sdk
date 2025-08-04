package com.smartling.api.v2.client.request;

import lombok.Data;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import java.net.URI;

@Data
public class RequestContext
{
    private final String method;
    private final URI uri;
    private final MultivaluedMap<String, Object> headers;

    public static RequestContext fromClientRequestContext(ClientRequestContext context)
    {
        MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<>();
        headers.putAll(context.getHeaders());

        return new RequestContext(context.getMethod(), context.getUri(), headers);
    }
}
