package com.smartling.api.contexts.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import com.smartling.api.contexts.v2.pto.BatchBindingPTO;
import com.smartling.api.contexts.v2.pto.BatchBindingRequestPTO;
import com.smartling.api.contexts.v2.pto.BatchDeleteBindingsRequestPTO;
import com.smartling.api.contexts.v2.pto.BindingPTO;
import com.smartling.api.contexts.v2.pto.BindingRequestPTO;
import com.smartling.api.contexts.v2.pto.BindingsRequestPTO;
import com.smartling.api.contexts.v2.pto.ContextPTO;
import com.smartling.api.contexts.v2.pto.ContextUploadPTO;
import com.smartling.api.contexts.v2.pto.CoordinatesPTO;
import com.smartling.api.contexts.v2.pto.ContextUploadAndMatchPTO;
import com.smartling.api.contexts.v2.pto.MatchIdPTO;
import com.smartling.api.contexts.v2.pto.MatchRequestPTO;
import com.smartling.api.contexts.v2.pto.MatchStatusPTO;
import com.smartling.api.contexts.v2.pto.PaginatedListResponse;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.v2.response.EmptyData;
import com.smartling.api.v2.response.ResponseBuilders;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ContextsApiTest
{
    private static final String PROJECT_UID = "project_uid";
    private static final String CONTEXT_UID = "context_uid";
    private static final String MATCH_ID = "match_id";

    private final static String UPLOAD_CONTEXT_RESPONSE = "{\n" +
        "  \"response\": {\n" +
        "    \"code\": \"SUCCESS\",\n" +
        "    \"data\": {\n" +
        "      \"contextUid\": \"context_uid\",\n" +
        "      \"contextType\": \"HTML\",\n" +
        "      \"name\": \"contextName\",\n" +
        "      \"title\": \"contextTitle\",\n" +
        "      \"projectId\": \"projectId1\",\n" +
        "      \"created\": \"2016-09-20T01:16:47Z\"\n" +
        "     }\n"  +
        "    }\n"  +
        "  }";

    private final static String CREATE_BINDINGS_RESPONSE = "{\n" +
        "  \"response\": {\n" +
        "    \"code\": \"SUCCESS\",\n" +
        "    \"data\": {\n" +
        "      \"created\": {\n" +
        "        \"totalCount\": 2,\n" +
        "        \"items\": [\n" +
        "        {\n" +
        "          \"bindingUid\": \"bindingUid1\",\n" +
        "          \"contextUid\": \"contextUid1\",\n" +
        "          \"stringHashcode\": \"stringHashcode1\",\n" +
        "          \"coordinates\": {\n" +
        "            \"top\": 1,\n" +
        "            \"left\": 2,\n" +
        "            \"width\": 3,\n" +
        "            \"height\": 4\n" +
        "          }\n" +
        "        },\n" +
        "        {\n" +
        "          \"bindingUid\": \"bindingUid2\",\n" +
        "          \"contextUid\": \"contextUid2\",\n" +
        "          \"stringHashcode\": \"stringHashcode2\",\n" +
        "          \"coordinates\": {\n" +
        "            \"top\": 4,\n" +
        "            \"left\": 3,\n" +
        "            \"width\": 2,\n" +
        "            \"height\": 1\n" +
        "          }\n" +
        "        }\n" +
        "        ]\n" +
        "      },\n" +
        "      \"errors\": {\n" +
        "        \"totalCount\": 0,\n" +
        "        \"items\": []\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}";

    private static final String LIST_BINDINGS_RESPONSE = "{\n" +
        "  \"response\": {\n" +
        "    \"code\": \"SUCCESS\",\n" +
        "    \"data\": {\n" +
        "      \"totalCount\": 3,\n" +
        "      \"items\": [\n" +
        "        {\n" +
        "          \"bindingUid\": \"binding1\",\n" +
        "          \"contextUid\": \"context_uid\",\n" +
        "          \"stringHashcode\": \"hashcode1\",\n" +
        "          \"contextPosition\": 100002,\n" +
        "          \"coordinates\": {\n" +
        "            \"top\": 1,\n" +
        "            \"left\": 2,\n" +
        "            \"width\": 3,\n" +
        "            \"height\": 4\n" +
        "          }\n" +
        "        }\n" +
        "      ]\n" +
        "    }\n" +
        "  }\n" +
        "}";

    private ContextsApi contextApi;
    private MockWebServer mockWebServer;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception
    {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        objectMapper = new ObjectMapper();

        ClientConfiguration config = DefaultClientConfiguration.builder().baseUrl(mockWebServer.url("/").url()).build();
        contextApi = new ContextsApiFactory().buildApi(new BearerAuthStaticTokenFilter("foo"), config);
    }

    @After
    public void tearDown() throws Exception
    {
        mockWebServer.shutdown();
    }

    private void assignResponse(String body)
    {
        MockResponse response = new MockResponse()
            .setResponseCode(HttpStatus.SC_OK)
            .setHeader(javax.ws.rs.core.HttpHeaders.CONTENT_LENGTH, body.length())
            .setHeader(javax.ws.rs.core.HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
            .setBody(body);

        mockWebServer.enqueue(response);
    }

    @Test
    public void testGetApiClass()
    {
        assertEquals(ContextsApi.class, new ContextsApiFactory().getApiClass());
    }

    @Test
    public void testUploadContext() throws InterruptedException
    {
        byte[] content = "<div>context content</div>".getBytes();
        assignResponse(UPLOAD_CONTEXT_RESPONSE);

        ContextPTO context = contextApi.uploadContext(PROJECT_UID,
            new ContextUploadPTO("context.html",  "2016-09-20T01:16:47Z", content)
        );

        assertThat(context.getContextType(), is("HTML"));
        assertThat(context.getContextUid(), is(CONTEXT_UID));
        assertThat(context.getName(), is("contextName"));
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertTrue(request.getPath().contains("/projects/" + PROJECT_UID + "/contexts"));
    }

    @Test
    public void testListContextsByProject() throws Exception
    {
        ContextPTO contextPTO = new ContextPTO("ignoredContextUid", "IMAGE", "someName", "09/20/2016 01:16:47.762");
        String body = objectMapper.writeValueAsString(ResponseBuilders.respondWith(new PaginatedListResponse<>(Lists.newArrayList(contextPTO), "0")));
        assignResponse(body);

        PaginatedListResponse<ContextPTO> contextListResponse = contextApi.listContextsByProject(PROJECT_UID, null, null, null);

        assertThat(contextListResponse.getOffset(), is("0"));
        assertThat(contextListResponse.getItems(), hasItem(contextPTO));
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertTrue(request.getPath().contains("/projects/" + PROJECT_UID + "/contexts"));
    }

    @Test
    public void testGetContext() throws Exception
    {
        String body = objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .writeValueAsString(ResponseBuilders.respondWith(new ContextPTO(CONTEXT_UID, "IMAGE", "image-file-name.png", "2017-02-02 00:00:01")));
        assignResponse(body);

        ContextPTO context = contextApi.getContext(PROJECT_UID, CONTEXT_UID);

        assertThat(context.getContextUid(), is(CONTEXT_UID));
        assertThat(context.getContextType(), is("IMAGE"));
        assertThat(context.getName(), is("image-file-name.png"));
        assertThat(context.getCreated(), is("2017-02-02 00:00:01"));

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertTrue(request.getPath().contains("/projects/" + PROJECT_UID + "/contexts/" + CONTEXT_UID));
    }

    @Test
    public void testDeleteContext() throws Exception
    {
        String body = objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .writeValueAsString(ResponseBuilders.respondWith(new EmptyData()));
        assignResponse(body);

        contextApi.deleteContext(PROJECT_UID, CONTEXT_UID);

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("DELETE", request.getMethod());
        assertTrue(request.getPath().contains("/projects/" + PROJECT_UID + "/contexts/" + CONTEXT_UID));
    }

    @Test
    public void testStreamResponse() throws Exception
    {
        assignResponse("test");

        try (InputStream stream = contextApi.downloadContextFileContent(PROJECT_UID, CONTEXT_UID))
        {
            String content = IOUtils.toString(stream, StandardCharsets.UTF_8);
            assertThat(content, is("test"));
        }
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertTrue(request.getPath().contains("/projects/" + PROJECT_UID + "/contexts/" + CONTEXT_UID + "/content"));

    }

    @Test
    public void testMatchAsync() throws Exception
    {
        String body = objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .writeValueAsString(ResponseBuilders.respondWith(new MatchIdPTO(MATCH_ID)));
        assignResponse(body);

        MatchIdPTO matchIdPTO = contextApi.matchAsync(PROJECT_UID, CONTEXT_UID, new MatchRequestPTO());

        assertThat(matchIdPTO.getMatchId(), is(MATCH_ID));
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertTrue(request.getPath().contains("/projects/" + PROJECT_UID + "/contexts/" + CONTEXT_UID + "/match/async"));
    }

    @Test
    public void testUploadAndMatchAsync() throws Exception
    {
        String body = objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .writeValueAsString(ResponseBuilders.respondWith(new MatchIdPTO(MATCH_ID)));
        assignResponse(body);
        byte[] content = "<div>context content</div>".getBytes();
        ContextUploadAndMatchPTO contextUploadPTO = new ContextUploadAndMatchPTO("context.html", content,
            Collections.<String>emptyList()
        );

        MatchIdPTO matchIdPTO = contextApi.uploadContextAndMatchAsync(PROJECT_UID, contextUploadPTO);

        assertThat(matchIdPTO.getMatchId(), is(MATCH_ID));
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertTrue(request.getPath().contains("/projects/" + PROJECT_UID + "/contexts/upload-and-match-async"));
    }

    @Test
    public void testMatchAsyncStatus() throws Exception
    {
        String body = objectMapper.writeValueAsString(ResponseBuilders.respondWith(new MatchStatusPTO(MATCH_ID, "2017-04-19T14:21:11Z", "2017-04-19T14:21:11Z", "COMPLETED", "contextId", 99L, null)));
        assignResponse(body);

        MatchStatusPTO matchStatusPTO = contextApi.matchAsyncStatus(PROJECT_UID, MATCH_ID);

        assertThat(matchStatusPTO.getMatchId(), is(MATCH_ID));
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertTrue(request.getPath().contains("/projects/" + PROJECT_UID + "/match/" + MATCH_ID));
   }

    @Test
    public void testCreateBinding() throws Exception
    {
        assignResponse(CREATE_BINDINGS_RESPONSE);

        BindingsRequestPTO bindingsRequestPTO = new BindingsRequestPTO(asList(
            new BindingRequestPTO("requestContext1", "requestString1", new CoordinatesPTO(1, 2, 3, 4)),
            new BindingRequestPTO("requestContext2", "requestString2", (CoordinatesPTO)null)
        ));

        BatchBindingPTO batchBindingResponse = contextApi.createBindings(PROJECT_UID, bindingsRequestPTO);

        assertThat(batchBindingResponse.getCreated().getTotalCount(), is(2L));
        List<BindingPTO> items = batchBindingResponse.getCreated().getItems();
        assertThat(items.size(), is(2));

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertTrue(request.getPath().contains("/projects/" + PROJECT_UID + "/bindings"));
    }

    @Test
    public void testListBindings() throws InterruptedException
    {
        assignResponse(LIST_BINDINGS_RESPONSE);

        contextApi.listBindings(PROJECT_UID,
            new BatchBindingRequestPTO(Collections.singletonList("hashcode1"), null, null, null), null
        );

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertTrue(request.getPath().contains("/projects/" + PROJECT_UID + "/bindings/list"));
    }

    @Test
    public void testDeleteBindings() throws Exception
    {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String body = objectMapper.writeValueAsString(ResponseBuilders.respondWith(new EmptyData()));
        assignResponse(body);

        contextApi.deleteBindings(PROJECT_UID, new BatchDeleteBindingsRequestPTO(asList("hash1", "hash2"), null, null, null));

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertTrue(request.getPath().contains("/projects/" + PROJECT_UID + "/bindings/remove"));

    }
}
