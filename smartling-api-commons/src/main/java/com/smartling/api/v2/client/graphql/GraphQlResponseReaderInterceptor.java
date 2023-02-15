package com.smartling.api.v2.client.graphql;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartling.api.v2.client.unmarshal.RestApiResponseReaderInterceptor;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Provider
@Priority(Priorities.USER)
public class GraphQlResponseReaderInterceptor extends RestApiResponseReaderInterceptor
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

        context.setInputStream(new ByteArrayInputStream(node.toString().getBytes(StandardCharsets.UTF_8)));
        return context.proceed();
    }
}
