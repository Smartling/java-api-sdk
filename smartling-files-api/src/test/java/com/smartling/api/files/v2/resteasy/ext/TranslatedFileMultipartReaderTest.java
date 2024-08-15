package com.smartling.api.files.v2.resteasy.ext;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Providers;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.jboss.resteasy.plugins.providers.multipart.MultipartConstants.MULTIPART_MIXED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TranslatedFileMultipartReaderTest
{
    @InjectMocks
    private TranslatedFileMultipartReader translatedFileMultipartReader = new TranslatedFileMultipartReader();

    @Mock
    protected Providers workers;

    @Before
    public void setUp()
    {
        initMocks(this);
    }

    @Test
    public void testIsReadable()
    {
        assertTrue(translatedFileMultipartReader.isReadable(TranslatedFileMultipart.class, TranslatedFileMultipart.class, null, null));
        assertFalse(translatedFileMultipartReader.isReadable(TranslatedFileMultipart.class, Integer.class, null, null));
    }

    @Test
    public void testReadFromWhenSuccess() throws IOException
    {
        final String boundary = UUID.randomUUID().toString();
        final String responseBody = "--" + boundary + "\r\n"
            + "Content-Type: application/octet-stream; charset=UTF-8\r\n"
            + "Content-Disposition: attachment; filename=\"myfile.properties\";\r\n"
            + "\r\n"
            + "key1=value1\nkey2=value2\r\n"
            + "--" + boundary + "\r\n"
            + "Content-Type: application/json; charset=UTF-8\r\n"
            + "\r\n"
            + "{\"key\":\"value\"}\r\n"
            + "--" + boundary + "--";

        final MediaType fileMediaType = MediaType.valueOf("application/octet-stream; charset=UTF-8");

        final MessageBodyReader<InputStream> inputStreamReader = mock(MessageBodyReader.class);
        when(inputStreamReader.isReadable(InputStream.class, null, null, fileMediaType)).thenReturn(true);
        when(inputStreamReader.readFrom(any(), any(), any(), any(), any(), any())).thenReturn(new ByteArrayInputStream("key1=value1\nkey2=value2".getBytes()));
        when(workers.getMessageBodyReader(InputStream.class, null, new Annotation[] {}, fileMediaType)).thenReturn(inputStreamReader);

        final MediaType metadataMediaType = MediaType.valueOf("application/json; charset=UTF-8");

        final TranslatedFileMetadata translatedFileMetadata = mock(TranslatedFileMetadata.class);
        final MessageBodyReader<TranslatedFileMetadata> metadataReader = mock(MessageBodyReader.class);
        when(metadataReader.isReadable(TranslatedFileMetadata.class, TranslatedFileMetadata.class, null, metadataMediaType)).thenReturn(true);
        when(metadataReader.readFrom(any(), any(), any(), any(), any(), any())).thenReturn(translatedFileMetadata);
        when(workers.getMessageBodyReader(TranslatedFileMetadata.class, TranslatedFileMetadata.class, new Annotation[] {}, metadataMediaType)).thenReturn(metadataReader);

        final TranslatedFileMultipart translatedFileMultipart = translatedFileMultipartReader.readFrom(
            TranslatedFileMultipart.class,
            TranslatedFileMultipart.class,
            null,
            MediaType.valueOf(MULTIPART_MIXED + "; boundary=" + boundary),
            null,
            new ByteArrayInputStream(responseBody.getBytes())
        );
        assertNotNull(translatedFileMultipart);
        assertSame(translatedFileMetadata, translatedFileMultipart.getTranslatedFileMetadata());

        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, singletonList("application/octet-stream; charset=UTF-8"));
        headers.put(HttpHeaders.CONTENT_DISPOSITION, singletonList("attachment; filename=\"myfile.properties\";"));
        assertEquals(headers, translatedFileMultipart.getFileHeaders());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM_TYPE.withCharset("UTF-8"), translatedFileMultipart.getFileMediaType());
        final InputStream inputStream = translatedFileMultipart.getFileBody();
        final String fileString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        assertEquals("key1=value1\nkey2=value2", fileString);
    }

    @Test(expected = IOException.class)
    public void testReadFromWhenFailedDueToBoundaryAbsence() throws IOException
    {
        final String boundary = UUID.randomUUID().toString();
        final String responseBody = "--" + boundary + "\r\n"
            + "Content-Type: application/octet-stream; charset=UTF-8\r\n"
            + "Content-Disposition: attachment; filename=\"myfile.properties\";\r\n"
            + "\r\n"
            + "key1=value1\nkey2=value2\r\n"
            + "--" + boundary + "\r\n"
            + "Content-Type: application/json; charset=UTF-8\r\n"
            + "\r\n"
            + "{\"key\":\"value\"}\r\n"
            + "--" + boundary + "--";

        final MediaType fileMediaType = MediaType.valueOf("application/octet-stream; charset=UTF-8");

        final MessageBodyReader<InputStream> inputStreamReader = mock(MessageBodyReader.class);
        when(inputStreamReader.isReadable(InputStream.class, null, null, fileMediaType)).thenReturn(true);
        when(inputStreamReader.readFrom(any(), any(), any(), any(), any(), any())).thenReturn(new ByteArrayInputStream("key1=value1\nkey2=value2".getBytes()));
        when(workers.getMessageBodyReader(InputStream.class, null, new Annotation[] {}, fileMediaType)).thenReturn(inputStreamReader);

        final MediaType metadataMediaType = MediaType.valueOf("application/json; charset=UTF-8");

        final TranslatedFileMetadata translatedFileMetadata = mock(TranslatedFileMetadata.class);
        final MessageBodyReader<TranslatedFileMetadata> metadataReader = mock(MessageBodyReader.class);
        when(metadataReader.isReadable(TranslatedFileMetadata.class, TranslatedFileMetadata.class, null, metadataMediaType)).thenReturn(true);
        when(metadataReader.readFrom(any(), any(), any(), any(), any(), any())).thenReturn(translatedFileMetadata);
        when(workers.getMessageBodyReader(TranslatedFileMetadata.class, TranslatedFileMetadata.class, new Annotation[] {}, metadataMediaType)).thenReturn(metadataReader);

        translatedFileMultipartReader.readFrom(
            TranslatedFileMultipart.class,
            TranslatedFileMultipart.class,
            null,
            MediaType.valueOf(MULTIPART_MIXED),
            null,
            new ByteArrayInputStream(responseBody.getBytes())
        );
    }

    @Test(expected = IOException.class)
    public void testReadFromWhenFailedDueToBigNumberOfParts() throws IOException
    {
        final String boundary = UUID.randomUUID().toString();
        final String responseBody = "--" + boundary + "\r\n"
            + "Content-Type: application/octet-stream; charset=UTF-8\r\n"
            + "Content-Disposition: attachment; filename=\"myfile.properties\";\r\n"
            + "\r\n"
            + "key1=value1\nkey2=value2\r\n"
            + "--" + boundary + "\r\n"
            + "Content-Type: application/json; charset=UTF-8\r\n"
            + "\r\n"
            + "{\"key\":\"value\"}\r\n"
            + "--" + boundary + "\r\n"
            + "Content-Type: application/json; charset=UTF-8\r\n"
            + "\r\n"
            + "{\"key\":\"value\"}\r\n"
            + "--" + boundary + "--";

        final MediaType fileMediaType = MediaType.valueOf("application/octet-stream; charset=UTF-8");

        final MessageBodyReader<InputStream> inputStreamReader = mock(MessageBodyReader.class);
        when(inputStreamReader.isReadable(InputStream.class, null, null, fileMediaType)).thenReturn(true);
        when(inputStreamReader.readFrom(any(), any(), any(), any(), any(), any())).thenReturn(new ByteArrayInputStream("key1=value1\nkey2=value2".getBytes()));
        when(workers.getMessageBodyReader(InputStream.class, null, new Annotation[] {}, fileMediaType)).thenReturn(inputStreamReader);

        final MediaType metadataMediaType = MediaType.valueOf("application/json; charset=UTF-8");

        final TranslatedFileMetadata translatedFileMetadata = mock(TranslatedFileMetadata.class);
        final MessageBodyReader<TranslatedFileMetadata> metadataReader = mock(MessageBodyReader.class);
        when(metadataReader.isReadable(TranslatedFileMetadata.class, TranslatedFileMetadata.class, null, metadataMediaType)).thenReturn(true);
        when(metadataReader.readFrom(any(), any(), any(), any(), any(), any())).thenReturn(translatedFileMetadata);
        when(workers.getMessageBodyReader(TranslatedFileMetadata.class, TranslatedFileMetadata.class, new Annotation[] {}, metadataMediaType)).thenReturn(metadataReader);

        translatedFileMultipartReader.readFrom(
            TranslatedFileMultipart.class,
            TranslatedFileMultipart.class,
            null,
            MediaType.valueOf(MULTIPART_MIXED + "; boundary=" + boundary),
            null,
            new ByteArrayInputStream(responseBody.getBytes())
        );
    }

    @Test(expected = IOException.class)
    public void testReadFromWhenFailedDueToSmallNumberOfParts() throws IOException
    {
        final String boundary = UUID.randomUUID().toString();
        final String responseBody = "--" + boundary + "\r\n"
            + "Content-Type: application/octet-stream; charset=UTF-8\r\n"
            + "Content-Disposition: attachment; filename=\"myfile.properties\";\r\n"
            + "\r\n"
            + "key1=value1\nkey2=value2\r\n"
            + "--" + boundary + "--";

        final MediaType fileMediaType = MediaType.valueOf("application/octet-stream; charset=UTF-8");

        final MessageBodyReader<InputStream> inputStreamReader = mock(MessageBodyReader.class);
        when(inputStreamReader.isReadable(InputStream.class, null, null, fileMediaType)).thenReturn(true);
        when(inputStreamReader.readFrom(any(), any(), any(), any(), any(), any())).thenReturn(new ByteArrayInputStream("key1=value1\nkey2=value2".getBytes()));
        when(workers.getMessageBodyReader(InputStream.class, null, new Annotation[] {}, fileMediaType)).thenReturn(inputStreamReader);

        final MediaType metadataMediaType = MediaType.valueOf("application/json; charset=UTF-8");

        final TranslatedFileMetadata translatedFileMetadata = mock(TranslatedFileMetadata.class);
        final MessageBodyReader<TranslatedFileMetadata> metadataReader = mock(MessageBodyReader.class);
        when(metadataReader.isReadable(TranslatedFileMetadata.class, TranslatedFileMetadata.class, null, metadataMediaType)).thenReturn(true);
        when(metadataReader.readFrom(any(), any(), any(), any(), any(), any())).thenReturn(translatedFileMetadata);
        when(workers.getMessageBodyReader(TranslatedFileMetadata.class, TranslatedFileMetadata.class, new Annotation[] {}, metadataMediaType)).thenReturn(metadataReader);

        translatedFileMultipartReader.readFrom(
            TranslatedFileMultipart.class,
            TranslatedFileMultipart.class,
            null,
            MediaType.valueOf(MULTIPART_MIXED + "; boundary=" + boundary),
            null,
            new ByteArrayInputStream(responseBody.getBytes())
        );
    }

    @Test(expected = IOException.class)
    public void testReadFromWhenFailedDueToInvalidMetadataContentType() throws IOException
    {
        final String boundary = UUID.randomUUID().toString();
        final String responseBody = "--" + boundary + "\r\n"
            + "Content-Type: application/octet-stream; charset=UTF-8\r\n"
            + "Content-Disposition: attachment; filename=\"myfile.properties\";\r\n"
            + "\r\n"
            + "key1=value1\nkey2=value2\r\n"
            + "--" + boundary + "\r\n"
            + "Content-Type: application/octet-stream; charset=UTF-8\r\n"
            + "\r\n"
            + "{\"key\":\"value\"}\r\n"
            + "--" + boundary + "--";

        final MediaType fileMediaType = MediaType.valueOf("application/octet-stream; charset=UTF-8");

        final MessageBodyReader<InputStream> inputStreamReader = mock(MessageBodyReader.class);
        when(inputStreamReader.isReadable(InputStream.class, null, null, fileMediaType)).thenReturn(true);
        when(inputStreamReader.readFrom(any(), any(), any(), any(), any(), any())).thenReturn(new ByteArrayInputStream("key1=value1\nkey2=value2".getBytes()));
        when(workers.getMessageBodyReader(InputStream.class, null, new Annotation[] {}, fileMediaType)).thenReturn(inputStreamReader);

        final MediaType metadataMediaType = MediaType.valueOf("application/octet-stream; charset=UTF-8");

        final TranslatedFileMetadata translatedFileMetadata = mock(TranslatedFileMetadata.class);
        final MessageBodyReader<TranslatedFileMetadata> metadataReader = mock(MessageBodyReader.class);
        when(metadataReader.isReadable(TranslatedFileMetadata.class, TranslatedFileMetadata.class, null, metadataMediaType)).thenReturn(true);
        when(metadataReader.readFrom(any(), any(), any(), any(), any(), any())).thenReturn(translatedFileMetadata);
        when(workers.getMessageBodyReader(TranslatedFileMetadata.class, TranslatedFileMetadata.class, new Annotation[] {}, metadataMediaType)).thenReturn(metadataReader);

        translatedFileMultipartReader.readFrom(
            TranslatedFileMultipart.class,
            TranslatedFileMultipart.class,
            null,
            MediaType.valueOf(MULTIPART_MIXED + "; boundary=" + boundary),
            null,
            new ByteArrayInputStream(responseBody.getBytes())
        );
    }

    @Test(expected = IOException.class)
    public void testReadFromWhenFailedDueToInvalidMetadataJson() throws IOException
    {
        final String boundary = UUID.randomUUID().toString();
        final String responseBody = "--" + boundary + "\r\n"
            + "Content-Type: application/octet-stream; charset=UTF-8\r\n"
            + "Content-Disposition: attachment; filename=\"myfile.properties\";\r\n"
            + "\r\n"
            + "key1=value1\nkey2=value2\r\n"
            + "--" + boundary + "\r\n"
            + "Content-Type: application/json; charset=UTF-8\r\n"
            + "\r\n"
            + "{\"key\":\"value\"}\r\n"
            + "--" + boundary + "--";

        final MediaType fileMediaType = MediaType.valueOf("application/octet-stream; charset=UTF-8");

        final MessageBodyReader<InputStream> inputStreamReader = mock(MessageBodyReader.class);
        when(inputStreamReader.isReadable(InputStream.class, null, null, fileMediaType)).thenReturn(true);
        when(inputStreamReader.readFrom(any(), any(), any(), any(), any(), any())).thenReturn(new ByteArrayInputStream("key1=value1\nkey2=value2".getBytes()));
        when(workers.getMessageBodyReader(InputStream.class, null, new Annotation[] {}, fileMediaType)).thenReturn(inputStreamReader);

        final MediaType metadataMediaType = MediaType.valueOf("application/json; charset=UTF-8");

        final MessageBodyReader<TranslatedFileMetadata> metadataReader = mock(MessageBodyReader.class);
        when(metadataReader.isReadable(TranslatedFileMetadata.class, TranslatedFileMetadata.class, null, metadataMediaType)).thenReturn(true);
        when(metadataReader.readFrom(any(), any(), any(), any(), any(), any())).thenReturn(null);
        when(workers.getMessageBodyReader(TranslatedFileMetadata.class, TranslatedFileMetadata.class, new Annotation[] {}, metadataMediaType)).thenReturn(metadataReader);

        translatedFileMultipartReader.readFrom(
            TranslatedFileMultipart.class,
            TranslatedFileMultipart.class,
            null,
            MediaType.valueOf(MULTIPART_MIXED + "; boundary=" + boundary),
            null,
            new ByteArrayInputStream(responseBody.getBytes())
        );
    }
}
