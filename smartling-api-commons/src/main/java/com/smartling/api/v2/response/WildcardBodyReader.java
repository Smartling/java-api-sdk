package com.smartling.api.v2.response;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Provider
@Consumes("*/*")
@Slf4j
public class WildcardBodyReader implements MessageBodyReader<Object> {
    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return mediaType.toString().equals("*/*");
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                           MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        String body = IOUtils.toString(entityStream, StandardCharsets.UTF_8.name());
        String annotationsView = "";
        for (Annotation annotation : annotations) {
            annotationsView += "\n\t\t" + annotation;
        }
        String message = "Unexpected media type " + mediaType + " for response:\n\ttype: " + type.getName()
            +"\n\tgenericType: " + genericType + "\n\tannotations: [" + annotationsView
            + "\n\t]\n\theaders: " + httpHeaders + "\n\tbody: " + body;
        log.error(message);
        throw new WebApplicationException(message);
    }
}
