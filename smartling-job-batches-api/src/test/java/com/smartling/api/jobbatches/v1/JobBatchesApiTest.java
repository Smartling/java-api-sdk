package com.smartling.api.jobbatches.v1;

import com.smartling.api.jobbatches.v1.pto.CreateBatchRequestPTO;
import com.smartling.api.jobbatches.v1.pto.CreateBatchResponsePTO;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;

import static javax.ws.rs.HttpMethod.POST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JobBatchesApiTest
{
    private final String SUCCESS_RESPONSE_ENVELOPE = "{\"response\":{\"code\":\"SUCCESS\",\"data\":%s}})";

    private static final String PROJECT_ID = "4bca2a7b8";

    private MockWebServer mockWebServer;
    private JobBatchesApi jobBatchesApi;

    @Before
    public void setUp() throws Exception
    {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final JobBatchesApiFactory factory = new JobBatchesApiFactory();
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("foo");

        jobBatchesApi = factory.buildApi(tokenFilter, mockWebServer.url("/").toString());
    }

    @After
    public void tearDown() throws Exception
    {
        mockWebServer.shutdown();
    }

    private void assignResponse(final int httpStatusCode, final String body)
    {
        final MockResponse response = new MockResponse()
            .setResponseCode(httpStatusCode)
            .setHeader(HttpHeaders.CONTENT_LENGTH, body.length())
            .setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
            .setBody(body);

        mockWebServer.enqueue(response);
    }

    @Test
    public void testCreateBatch() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, "{\"batchUid\" : \"createdBatchUid\"}"));

        // language=JSON
        String requestString = "{"
            + "\"translationJobUid\":\"jobUid\","
            + "\"authorize\":true"
            + "}";

        CreateBatchRequestPTO requestBody = CreateBatchRequestPTO.builder()
            .translationJobUid("jobUid")
            .authorize(true)
            .build();

        CreateBatchResponsePTO response = jobBatchesApi.createBatch(PROJECT_ID, requestBody);

        assertEquals("createdBatchUid", response.getBatchUid());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(POST, request.getMethod());
        assertEquals(requestString, request.getBody().readUtf8());
        assertTrue(request.getPath().startsWith(("/job-batches-api/v1/projects/" + PROJECT_ID + "/batches")));
    }
}
