package com.smartling.api.jobbatches.v2;

import com.smartling.api.jobbatches.v2.pto.CancelBatchActionRequestPTO;
import com.smartling.api.jobbatches.v2.pto.CreateBatchRequestPTO;
import com.smartling.api.jobbatches.v2.pto.CreateBatchResponsePTO;
import com.smartling.api.jobbatches.v2.pto.RegisterBatchActionRequestPTO;
import com.smartling.api.jobbatches.v2.pto.WorkflowPTO;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;
import java.util.Collections;

import static javax.ws.rs.HttpMethod.POST;
import static javax.ws.rs.HttpMethod.PUT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JobBatchesApiTest
{
    private final String SUCCESS_RESPONSE_ENVELOPE = "{\"response\":{\"code\":\"SUCCESS\",\"data\":%s}})";

    private static final String PROJECT_ID = "4bca2a7b8";
    private static final String BATCH_UID = "foobar";

    private MockWebServer mockWebServer;
    private JobBatchesApi jobBatchesApi;

    @Before
    public void setUp() throws Exception
    {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final JobBatchesApiFactory factory = new JobBatchesApiFactory();
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("foo");
        final ClientConfiguration config = DefaultClientConfiguration.builder()
            .baseUrl(mockWebServer.url("/").url())
            .build();

        jobBatchesApi = factory.buildApi(tokenFilter, config);
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
            + "\"authorize\":true,"
            + "\"fileUris\":[\"fileUri.json\"],"
            + "\"localeWorkflows\":[{\"targetLocaleId\":\"fr-FR\",\"workflowUid\":\"workflowUid\"}]"
            + "}";

        CreateBatchRequestPTO requestBody = CreateBatchRequestPTO.builder()
            .translationJobUid("jobUid")
            .authorize(true)
            .fileUris(Collections.singletonList("fileUri.json"))
            .localeWorkflows(Collections.singletonList(WorkflowPTO.builder()
                    .targetLocaleId("fr-FR")
                    .workflowUid("workflowUid")
                    .build()
            ))
            .build();

        CreateBatchResponsePTO response = jobBatchesApi.createBatch(PROJECT_ID, requestBody);

        assertEquals("createdBatchUid", response.getBatchUid());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(POST, request.getMethod());
        assertEquals(requestString, request.getBody().readUtf8());
        assertTrue(request.getPath().startsWith(("/job-batches-api/v2/projects/" + PROJECT_ID + "/batches")));
    }

    @Test
    public void testRegisterFile() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, "{}"));

        // language=JSON
        String expectedRequest = "{"
            + "\"action\":\"REGISTER_FILE\","
            + "\"fileUri\":\"fileUri.json\""
            + "}";

        RegisterBatchActionRequestPTO requestBody = RegisterBatchActionRequestPTO.builder()
            .fileUri("jobUid")
            .fileUri("fileUri.json")
            .build();

        jobBatchesApi.registerFile(PROJECT_ID, BATCH_UID, requestBody);

        RecordedRequest actualRequest = mockWebServer.takeRequest();
        assertEquals(PUT, actualRequest.getMethod());
        assertEquals(expectedRequest, actualRequest.getBody().readUtf8());
        assertTrue(actualRequest.getPath().startsWith(("/job-batches-api/v2/projects/" + PROJECT_ID + "/batches/" + BATCH_UID)));
    }

    @Test
    public void testCancelFile() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, "{}"));

        // language=JSON
        String expectedRequest = "{"
            + "\"action\":\"CANCEL_FILE\","
            + "\"fileUri\":\"fileUri.json\","
            + "\"reason\":\"Burn it!!!\""
            + "}";

        CancelBatchActionRequestPTO requestBody = CancelBatchActionRequestPTO.builder()
            .fileUri("jobUid")
            .fileUri("fileUri.json")
            .reason("Burn it!!!")
            .build();

        jobBatchesApi.cancelFile(PROJECT_ID, BATCH_UID, requestBody);

        RecordedRequest actualRequest = mockWebServer.takeRequest();
        assertEquals(PUT, actualRequest.getMethod());
        assertEquals(expectedRequest, actualRequest.getBody().readUtf8());
        assertTrue(actualRequest.getPath().startsWith(("/job-batches-api/v2/projects/" + PROJECT_ID + "/batches/" + BATCH_UID)));
    }
}
