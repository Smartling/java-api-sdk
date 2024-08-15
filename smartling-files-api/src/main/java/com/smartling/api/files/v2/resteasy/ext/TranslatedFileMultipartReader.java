package com.smartling.api.files.v2.resteasy.ext;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartInputImpl;
import org.jboss.resteasy.plugins.providers.multipart.i18n.Messages;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import static java.lang.String.format;
import static org.jboss.resteasy.plugins.providers.multipart.MultipartConstants.MULTIPART_MIXED;

@Provider
@Consumes(MULTIPART_MIXED)
public class TranslatedFileMultipartReader implements MessageBodyReader<TranslatedFileMultipart>
{
    private static final String BOUNDARY_PARAMETER = "boundary";

    protected @Context Providers workers;

    @Override
    public boolean isReadable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType)
    {
        return type.equals(TranslatedFileMultipart.class);
    }

    @Override
    public TranslatedFileMultipart readFrom(Class<TranslatedFileMultipart> clazz, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException
    {
        final String boundary = mediaType.getParameters().get(BOUNDARY_PARAMETER);
        if (null == boundary)
            throw new IOException(Messages.MESSAGES.unableToGetBoundary());
        final MultipartInputImpl multipartInput = new MultipartInputImpl(mediaType, workers);
        multipartInput.parse(inputStream);

        if (multipartInput.getParts().size() != 2)
            throw new IOException(format("The response contains unexpected number of parts (%d). Two parts are expected. The first one is file's body, the second one is metadata object.", multipartInput.getParts().size()));

        final InputPart filePart = multipartInput.getParts().get(0);
        final InputPart metadataPart = multipartInput.getParts().get(1);

        final TranslatedFileMultipart translatedFileMultipart = new TranslatedFileMultipart();
        translatedFileMultipart.multipartInput = multipartInput;
        translatedFileMultipart.fileBody = filePart.getBody(InputStream.class, null);
        translatedFileMultipart.fileHeaders = filePart.getHeaders();
        translatedFileMultipart.fileMediaType = filePart.getMediaType();

        final MediaType metadataPartMediaType = metadataPart.getMediaType();
        if (null == metadataPartMediaType)
            throw new IOException("Missing content type of metadata part.");

        if (!StringUtils.equalsIgnoreCase(MediaType.APPLICATION_JSON_TYPE.getType(), metadataPartMediaType.getType()) || !StringUtils.equalsIgnoreCase(MediaType.APPLICATION_JSON_TYPE.getSubtype(), metadataPartMediaType.getSubtype()))
            throw new IOException(format("Unexpected content type of metadata part (%s). Expected (%s) metadata part.", metadataPart.getMediaType(), MediaType.APPLICATION_JSON_TYPE));

        translatedFileMultipart.translatedFileMetadata = metadataPart.getBody(TranslatedFileMetadata.class, TranslatedFileMetadata.class);
        if (null == translatedFileMultipart.translatedFileMetadata)
            throw new IOException("Cannot construct metadata json object.");

        return translatedFileMultipart;
    }
}
