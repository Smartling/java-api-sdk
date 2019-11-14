package com.smartling.api.jobs.v3;

import com.smartling.api.jobs.v3.pto.TranslationJobCreateCommandPTO;
import com.smartling.api.jobs.v3.pto.TranslationJobCreateResponsePTO;
import com.smartling.api.jobs.v3.pto.TranslationJobCustomFieldPTO;
import com.smartling.api.jobs.v3.pto.TranslationJobGetResponsePTO;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;
import java.util.Arrays;

import static javax.ws.rs.HttpMethod.POST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TranslationJobsApiTest
{

    private final String SUCCESS_RESPONSE_ENVELOPE = "{\"response\":{\"code\":\"SUCCESS\",\"data\":%s}})";

    private final String GET_TRANSLATION_JOB_RESPONSE_BODY = "{\n"
        + "            \"translationJobUid\": \"translationJobUid\",\n"
        + "            \"jobName\": \"jobName\",\n"
        + "            \"jobNumber\": \"PP-11111\",\n"
        + "            \"targetLocaleIds\": [\"fr-FR\"],\n"
        + "            \"description\": \"jobDescription\",\n"
        + "            \"dueDate\": \"2015-11-21T0B1:51:17Z\",\n"
        + "            \"referenceNumber\": \"referenceNumber\",\n"
        + "            \"callbackUrl\": \"https://www.callback.com/smartling/job\",\n"
        + "            \"callbackMethod\": \"GET\",\n"
        + "            \"createdDate\": \"2015-11-21T0B1:51:17Z\",\n"
        + "            \"modifiedDate\": \"2015-11-21T0B1:51:17Z\",\n"
        + "            \"createdByUserUid\": \"createdUserUid\",\n"
        + "            \"modifiedByUserUid\": \"modifiedUserUid\",\n"
        + "            \"firstCompletedDate\": \"2015-11-21T0B1:51:17Z\",\n"
        + "            \"lastCompletedDate\": \"2015-11-21T0B1:51:17Z\",\n"
        + "            \"jobStatus\" : \"IN_PROGRESS\",\n"
        + "            \"sourceFiles\" : [{\n"
        + "                \"uri\" : \"/file/app.properties\",\n"
        + "                \"name\" : \"/file/app.properties\"\n"
        + "             }],\n"
        + "             \"customFields\": [{\"fieldUid\": \"field1\", \"fieldName\":\"field name 1\", \"fieldValue\": \"value1\"}, {\"fieldUid\": \"field2\", \"fieldName\":\"field name 2\", \"fieldValue\": \"value2\"}]\n"
        + "        }";
    ;

    String PROJECT_ID = "4bca2a7b8";
    String TRANSLATION_JOB_UID = "0123456789ab";

    private MockWebServer mockWebServer;
    private TranslationJobsApi translationJobsApi;

    @Before
    public void setUp() throws Exception
    {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final TranslationJobsApiFactory factory = new TranslationJobsApiFactory();
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("foo");

        translationJobsApi = factory.buildApi(tokenFilter, mockWebServer.url("/").toString());
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
    public void testGetTranslationJob()
    {
        assignResponse(200, String.format(SUCCESS_RESPONSE_ENVELOPE, GET_TRANSLATION_JOB_RESPONSE_BODY));

        TranslationJobGetResponsePTO response = translationJobsApi.getTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID);

        assertEquals("translationJobUid", response.getTranslationJobUid());
        assertEquals("jobName", response.getJobName());
        assertEquals("PP-11111", response.getJobNumber());
        assertEquals(1, response.getTargetLocaleIds().size());
        assertEquals("fr-FR", response.getTargetLocaleIds().get(0));
        assertEquals("jobDescription", response.getDescription());
        assertEquals("2015-11-21T0B1:51:17Z", response.getDueDate());
        assertEquals("referenceNumber", response.getReferenceNumber());
        assertEquals("https://www.callback.com/smartling/job", response.getCallbackUrl());
        assertEquals("GET", response.getCallbackMethod());
        assertEquals("2015-11-21T0B1:51:17Z", response.getCreatedDate());
        assertEquals("2015-11-21T0B1:51:17Z", response.getModifiedDate());
        assertEquals("createdUserUid", response.getCreatedByUserUid());
        assertEquals("modifiedUserUid", response.getModifiedByUserUid());
        assertEquals("2015-11-21T0B1:51:17Z", response.getFirstCompletedDate());
        assertEquals("2015-11-21T0B1:51:17Z", response.getLastCompletedDate());
        assertEquals("IN_PROGRESS", response.getJobStatus());
        assertEquals(1, response.getSourceFiles().size());
        assertEquals("/file/app.properties", response.getSourceFiles().get(0).getUri());
        assertEquals("/file/app.properties", response.getSourceFiles().get(0).getName());
    }

    @Test
    public void testCreateTranslationJob() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, GET_TRANSLATION_JOB_RESPONSE_BODY));

        String requestString = "{"
            + "\"jobName\":\"jobName\","
            + "\"targetLocaleIds\":[\"fr-FR\"],"
            + "\"description\":\"jobDescription\","
            + "\"dueDate\":\"2015-11-21T0B1:51:17Z\","
            + "\"referenceNumber\":\"referenceNumber\","
            + "\"callbackUrl\":\"https://www.callback.com/smartling/job\","
            + "\"callbackMethod\":\"GET\","
            + "\"customFields\":[{\"fieldUid\":\"field1\",\"fieldName\":\"fieldName1\",\"fieldValue\":\"value1\"},{\"fieldUid\":\"field2\",\"fieldName\":\"fieldName2\",\"fieldValue\":\"value2\"}]"
            + "}";

        TranslationJobCreateCommandPTO requestBody = TranslationJobCreateCommandPTO.builder()
            .jobName("jobName")
            .targetLocaleIds(Arrays.asList("fr-FR"))
            .description("jobDescription")
            .dueDate("2015-11-21T0B1:51:17Z")
            .referenceNumber("referenceNumber")
            .callbackUrl("https://www.callback.com/smartling/job")
            .callbackMethod("GET")
            .customFields(Arrays.asList(TranslationJobCustomFieldPTO.builder()
                    .fieldUid("field1")
                    .fieldName("fieldName1")
                    .fieldValue("value1")
                    .build(),
                TranslationJobCustomFieldPTO.builder()
                    .fieldUid("field2")
                    .fieldName("fieldName2")
                    .fieldValue("value2")
                    .build()))
            .build();

        TranslationJobCreateResponsePTO response = translationJobsApi.createTranslationJob(PROJECT_ID, requestBody);

        assertEquals("translationJobUid", response.getTranslationJobUid());
        assertEquals("jobName", response.getJobName());
        assertEquals("PP-11111", response.getJobNumber());
        assertEquals(1, response.getTargetLocaleIds().size());
        assertEquals("fr-FR", response.getTargetLocaleIds().get(0));
        assertEquals("jobDescription", response.getDescription());
        assertEquals("2015-11-21T0B1:51:17Z", response.getDueDate());
        assertEquals("referenceNumber", response.getReferenceNumber());
        assertEquals("https://www.callback.com/smartling/job", response.getCallbackUrl());
        assertEquals("GET", response.getCallbackMethod());
        assertEquals("2015-11-21T0B1:51:17Z", response.getCreatedDate());
        assertEquals("2015-11-21T0B1:51:17Z", response.getModifiedDate());
        assertEquals("createdUserUid", response.getCreatedByUserUid());
        assertEquals("modifiedUserUid", response.getModifiedByUserUid());
        assertEquals("2015-11-21T0B1:51:17Z", response.getFirstCompletedDate());
        assertEquals("2015-11-21T0B1:51:17Z", response.getLastCompletedDate());
        assertEquals("IN_PROGRESS", response.getJobStatus());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(POST, request.getMethod());
        assertEquals(requestString, request.getBody().readUtf8());
        assertTrue(request.getPath().startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOBS_ENDPOINT).replace("{projectId}", PROJECT_ID)));

    }
}
