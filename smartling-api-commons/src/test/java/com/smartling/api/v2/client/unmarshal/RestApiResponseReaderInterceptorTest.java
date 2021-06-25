package com.smartling.api.v2.client.unmarshal;

import com.smartling.api.v2.response.EmptyData;
import com.smartling.api.v2.response.Response;
import com.smartling.api.v2.response.RestApiResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ReaderInterceptorContext;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RestApiResponseReaderInterceptorTest
{
    private static final String RESPONSE_JSON = "{ "
            + "   \"response\":{ "
            + "      \"code\":\"SUCCESS\", "
            + "      \"data\":{ "
            + "            \"packageUid\": \"2383bcd09\" "
            + "      } "
            + "   } "
            + "}";

    private static final String RESPONSE_NULL_JSON = "{ "
            + "   \"response\":{ "
            + "      \"code\":\"SUCCESS\", "
            + "      \"data\": null"
            + "   } "
            + "}";

    private static final String RESPONSE_ERROR_JSON = "{\n" +
        "    \"response\": {\n" +
        "        \"code\":\"VALIDATION_ERROR\",\n" +
        "        \"errors\": [\n" +
        "            {\n" +
        "                \"key\":\"file.not.found\",\n" +
        "                \"message\":\"The file \\\"e5988be8-117d-4755-aa7c-f0959f25d5f1:/content/launches/2021/06/21/test0/content/we-retail/language-masters/fr/test\\\" could not be found\",\n" +
        "                \"details\": {\n" +
        "                    \"field\":\"fileUri\"\n" +
        "                }\n" +
        "            }\n" +
        "        ]\n" +
        "    }\n" +
        "}";

    private static final String STRIPPED_RESPONSE_JSON = "{\"code\":\"SUCCESS\",\"data\":{\"packageUid\":\"2383bcd09\"}}";
    private static final String STRIPPED_DATA_JSON = "{\"packageUid\":\"2383bcd09\"}";
    private static final String STRIPPED_NULL_JSON = "null";
    private static final String STRIPPED_ERROR_JSON = "{\"code\":\"VALIDATION_ERROR\",\"errors\":[{\"key\":\"file.not.found\",\"message\":\"The file \\\"e5988be8-117d-4755-aa7c-f0959f25d5f1:/content/launches/2021/06/21/test0/content/we-retail/language-masters/fr/test\\\" could not be found\",\"details\":{\"field\":\"fileUri\"}}]}";

    private static final String RESPONSE_TEXT_PLAIN = "some text";



    @Mock
    private ReaderInterceptorContext context;

    private RestApiResponseReaderInterceptor interceptor;

    private void setContextJsonInputStream(final String json, final String strippedJson) throws Exception
    {
        final byte[] bytes = json.getBytes("UTF-8");
        final ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
        final MultivaluedMap<String, String> headers = new MultivaluedMapImpl<>();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON + ";charset=UTF-8");

        when(context.getHeaders()).thenReturn(headers);
        when(context.getInputStream()).thenReturn(byteInputStream);
        doAnswer(new Answer() {
                     @Override
                     public Object answer(final InvocationOnMock invocation) throws Throwable
                     {
                         final InputStream inputStream = (InputStream)invocation.getArguments()[0];
                         assertNotNull(inputStream);

                         final String value = new String(IOUtils.toByteArray(inputStream), "UTF-8");

                         assertEquals(strippedJson, value);
                         return null;
                     }
                 }).when(context).setInputStream(any(InputStream.class));
    }

    private void setContextInputStream(final String responseBody, final String contentType) throws Exception
    {
        final byte[] bytes = responseBody.getBytes("UTF-8");
        final ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
        final MultivaluedMap<String, String> headers = new MultivaluedMapImpl<>();
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        when(context.getHeaders()).thenReturn(headers);
        when(context.getInputStream()).thenReturn(byteInputStream);
    }

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        interceptor = new RestApiResponseReaderInterceptor();
    }

    @Test
    public void testAroundReadFromRestApiResponse() throws Exception
    {
        doReturn(RestApiResponse.class).when(context).getType();
        interceptor.aroundReadFrom(context);

        verify(context, times(1)).proceed();
        verify(context, never()).setInputStream(any(InputStream.class));
    }

    @Test
    public void testAroundReadFromInputStreamResponse() throws Exception
    {
        doReturn(InputStream.class).when(context).getType();
        interceptor.aroundReadFrom(context);

        verify(context, times(1)).proceed();
        verify(context, never()).setInputStream(any(InputStream.class));
    }

    @Test
    public void testAroundReadFromResponse() throws Exception
    {
        doReturn(Response.class).when(context).getType();
        setContextJsonInputStream(RESPONSE_JSON, STRIPPED_RESPONSE_JSON);
        interceptor.aroundReadFrom(context);

        verify(context, times(1)).proceed();
        verify(context, times(1)).setInputStream(any(InputStream.class));
    }

    @Test
    public void testAroundRead() throws Exception
    {
        doReturn(Object.class).when(context).getType();
        setContextJsonInputStream(RESPONSE_JSON, STRIPPED_DATA_JSON);
        interceptor.aroundReadFrom(context);

        verify(context, times(1)).proceed();
        verify(context, times(1)).setInputStream(any(InputStream.class));
    }

    @Test
    public void testAroundReadNullData() throws Exception
    {
        doReturn(Object.class).when(context).getType();
        setContextJsonInputStream(RESPONSE_NULL_JSON, STRIPPED_NULL_JSON);
        interceptor.aroundReadFrom(context);

        verify(context, times(1)).proceed();
        verify(context, times(1)).setInputStream(any(InputStream.class));
    }

    @Test
    public void testAroundReadErrorData() throws Exception
    {
        doReturn(String.class).when(context).getType();
        setContextJsonInputStream(RESPONSE_ERROR_JSON, STRIPPED_ERROR_JSON);
        interceptor.aroundReadFrom(context);

        verify(context, times(1)).proceed();
        verify(context, times(1)).setInputStream(any(InputStream.class));
    }

    @Test
    public void testAroundReadEmptyData() throws Exception
    {
        doReturn(EmptyData.class).when(context).getType();
        setContextJsonInputStream(RESPONSE_NULL_JSON, STRIPPED_NULL_JSON);
        assertEquals(RestApiResponseReaderInterceptor.EMPTY_DATA, interceptor.aroundReadFrom(context));

        verify(context, never()).proceed();
        verify(context, never()).setInputStream(any(InputStream.class));
    }

    @Test
    public void testAroundReadNonJsonData() throws Exception
    {
        doReturn(Object.class).when(context).getType();
        setContextInputStream(RESPONSE_TEXT_PLAIN, MediaType.TEXT_PLAIN);
        interceptor.aroundReadFrom(context);

        verify(context, times(1)).proceed();
        verify(context, never()).setInputStream(any(InputStream.class));
    }
}
