package com.smartling.api.external.client.graphql;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HeaderElement;
import org.apache.http.message.BasicHeader;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Provider
@Priority(Priorities.USER)
public class GraphQlResponseReaderInterceptor implements ReaderInterceptor
{
    @Override
    public Object aroundReadFrom(final ReaderInterceptorContext context) throws IOException, WebApplicationException
    {
        if (!MediaType.APPLICATION_JSON.equalsIgnoreCase(getResponseContentType(context)))
            return context.proceed();

        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode requestNode = mapper.readTree(context.getInputStream());

        JsonNode node = requestNode.get("data");

        if (node.isNull())
            return context.proceed();

        context.setInputStream(new ByteArrayInputStream(node.toString().getBytes("UTF-8")));
        return context.proceed();
    }

    private String getResponseContentType(final ReaderInterceptorContext context)
    {
        String contentType = null;
        List<String> contentTypeHeaders = context.getHeaders() == null ? null : context.getHeaders().get(HttpHeaders.CONTENT_TYPE);
        if (contentTypeHeaders != null && !contentTypeHeaders.isEmpty())
        {
            BasicHeader header = new BasicHeader(HttpHeaders.CONTENT_TYPE, contentTypeHeaders.get(0));
            HeaderElement[] helems = header.getElements();
            if (helems.length > 0)
            {
                contentType = helems[0].getName();
            }
        }
        return contentType;
    }
}
