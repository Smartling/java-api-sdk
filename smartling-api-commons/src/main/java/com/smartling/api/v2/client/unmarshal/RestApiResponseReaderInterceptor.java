package com.smartling.api.v2.client.unmarshal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartling.api.v2.response.EmptyData;
import com.smartling.api.v2.response.Response;
import com.smartling.api.v2.response.RestApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HeaderElement;
import org.apache.http.HttpHeaders;
import org.apache.http.message.BasicHeader;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Provides a response reader interceptor to remove the response wrapper
 * from Smartling API responses.
 */
@Provider
@Priority(Priorities.USER)
@Slf4j
public class RestApiResponseReaderInterceptor implements ReaderInterceptor
{
    protected static final EmptyData EMPTY_DATA = new EmptyData();

    @Override
    public Object aroundReadFrom(final ReaderInterceptorContext context) throws IOException, WebApplicationException
    {
        try
        {
            if (!MediaType.APPLICATION_JSON.equalsIgnoreCase(getResponseContentType(context)))
                return context.proceed();

            if (RestApiResponse.class.isAssignableFrom(context.getType()))
                return context.proceed();

            if (InputStream.class.isAssignableFrom(context.getType()))
                return context.proceed();

            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode requestNode = mapper.readTree(context.getInputStream());

            JsonNode node = requestNode.get("response");

            if (!Response.class.isAssignableFrom(context.getType()) && node.has("data"))
                node = node.get("data");

            if (node.isNull() && EmptyData.class.isAssignableFrom(context.getType()))
                return EMPTY_DATA;

            context.setInputStream(new ByteArrayInputStream(node.toString().getBytes(StandardCharsets.UTF_8)));
            return context.proceed();
        }
        catch (ProcessingException ex)
        {
            String body = IOUtils.toString(context.getInputStream(), StandardCharsets.UTF_8);
            String annotationsView = "";
            for (Annotation annotation : context.getAnnotations())
            {
                annotationsView += "\n\t\t" + annotation;
            }
            String message = "Error during response processing:\n\ttype: " + context.getType().getName()
                + "\n\tgenericType: " + context.getGenericType() + "\n\tannotations: [" + annotationsView
                + "\n\t]\n\theaders: " + context.getHeaders() + "\n\tmedia type: " + context.getMediaType()
                + "\n\tbody: " + body;
            log.error(message);
            throw new ProcessingException(message, ex);
        }
    }

    protected String getResponseContentType(final ReaderInterceptorContext context)
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
