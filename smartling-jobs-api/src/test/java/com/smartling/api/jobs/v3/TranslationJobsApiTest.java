package com.smartling.api.jobs.v3;

import com.smartling.api.jobs.v3.pto.AccountTranslationJobListItemPTO;
import com.smartling.api.jobs.v3.pto.AddLocaleCommandPTO;
import com.smartling.api.jobs.v3.pto.AsyncResponsePTO;
import com.smartling.api.jobs.v3.pto.ContentProgressReportPTO;
import com.smartling.api.jobs.v3.pto.CustomFieldAssignmentPTO;
import com.smartling.api.jobs.v3.pto.FileUriPTO;
import com.smartling.api.jobs.v3.pto.HashcodesAndLocalesPTO;
import com.smartling.api.jobs.v3.pto.LocaleAndHashcodeListCommandPTO;
import com.smartling.api.jobs.v3.pto.LocaleContentProgressReportPTO;
import com.smartling.api.jobs.v3.pto.LocaleHashcodePairPTO;
import com.smartling.api.jobs.v3.pto.LocaleWorkflowCommandPTO;
import com.smartling.api.jobs.v3.pto.PagingCommandPTO;
import com.smartling.api.jobs.v3.pto.SortCommandPTO;
import com.smartling.api.jobs.v3.pto.StringModifiedCountResponsePTO;
import com.smartling.api.jobs.v3.pto.TranslationJobAddFileCommandPTO;
import com.smartling.api.jobs.v3.pto.TranslationJobAuthorizeCommandPTO;
import com.smartling.api.jobs.v3.pto.TranslationJobCancelCommandPTO;
import com.smartling.api.jobs.v3.pto.TranslationJobCreateCommandPTO;
import com.smartling.api.jobs.v3.pto.TranslationJobCreateResponsePTO;
import com.smartling.api.jobs.v3.pto.TranslationJobCustomFieldPTO;
import com.smartling.api.jobs.v3.pto.TranslationJobFindByLocalesAndHashcodesCommandPTO;
import com.smartling.api.jobs.v3.pto.TranslationJobFoundByStringsAndLocalesResponsePTO;
import com.smartling.api.jobs.v3.pto.TranslationJobGetResponsePTO;
import com.smartling.api.jobs.v3.pto.TranslationJobListCommandPTO;
import com.smartling.api.jobs.v3.pto.TranslationJobListItemPTO;
import com.smartling.api.jobs.v3.pto.TranslationJobRemoveFileCommandPTO;
import com.smartling.api.jobs.v3.pto.TranslationJobSearchCommandPTO;
import com.smartling.api.jobs.v3.pto.TranslationJobUpdateCommandPTO;
import com.smartling.api.jobs.v3.pto.WorkflowProgressReportPTO;
import com.smartling.api.jobs.v3.pto.WorkflowStepSummaryReportItemPTO;
import com.smartling.api.jobs.v3.pto.account.AccountTranslationJobListCommandPTO;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.v2.response.EmptyData;
import com.smartling.api.v2.response.ListResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;
import java.util.Arrays;
import java.util.List;

import static com.smartling.api.jobs.v3.SampleApiResponses.GET_TRANSLATION_JOB_RESPONSE_BODY;
import static com.smartling.api.jobs.v3.SampleApiResponses.SUCCESS_RESPONSE_ENVELOPE;
import static java.util.Arrays.asList;
import static javax.ws.rs.HttpMethod.DELETE;
import static javax.ws.rs.HttpMethod.GET;
import static javax.ws.rs.HttpMethod.POST;
import static javax.ws.rs.HttpMethod.PUT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TranslationJobsApiTest
{

    private static final String PROJECT_ID = "4bca2a7b8";
    private static final String ACCOUNT_UID = "a11223344";
    private static final String TRANSLATION_JOB_UID = "0123456789ab";
    private static final String HASHCODE = "hash1";
    private static final String TRANSLATION_JOB_UID1 = "jnw83gm9we9r";
    private static final String TARGET_LOCALE_ID = "fr-FR";
    private static final String TARGET_LOCALE_ID_2 = "de-DE";

    private MockWebServer mockWebServer;
    private TranslationJobsApi translationJobsApi;

    @Before
    public void setUp() throws Exception
    {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final TranslationJobsApiFactory factory = new TranslationJobsApiFactory();
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("foo");
        final ClientConfiguration config = DefaultClientConfiguration.builder().baseUrl(mockWebServer.url("/").url()).build();

        translationJobsApi = factory.buildApi(tokenFilter, config);
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
            .targetLocaleIds(asList("fr-FR"))
            .description("jobDescription")
            .dueDate("2015-11-21T0B1:51:17Z")
            .referenceNumber("referenceNumber")
            .callbackUrl("https://www.callback.com/smartling/job")
            .callbackMethod("GET")
            .customFields(asList(TranslationJobCustomFieldPTO.builder()
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

    @Test
    public void testListTranslationJobs() throws InterruptedException
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.GET_TRANSLATION_JOBS_LIST_RESPONSE_BODY));
        TranslationJobListCommandPTO filterBody = new TranslationJobListCommandPTO("job2", "TT-123", asList("jobUid"), asList("DRAFT", "IN_PROGRESS"));
        PagingCommandPTO pagingBody = new PagingCommandPTO(10, 100);
        SortCommandPTO sortBody = new SortCommandPTO("jobName", "asc");

        ListResponse<TranslationJobListItemPTO> listResponse = translationJobsApi.listTranslationJobs(PROJECT_ID, filterBody, pagingBody, sortBody);

        assertEquals(listResponse.getTotalCount(), 2);

        assertEquals("translationJobUid1", listResponse.getItems().get(0).getTranslationJobUid());
        assertEquals("jobName1", listResponse.getItems().get(0).getJobName());
        assertEquals("PP-11111", listResponse.getItems().get(0).getJobNumber());
        assertEquals(asList("fr-FR"), listResponse.getItems().get(0).getTargetLocaleIds());
        assertEquals("IN_PROGRESS", listResponse.getItems().get(0).getJobStatus());
        assertEquals("2015-11-22T11:51:17Z", listResponse.getItems().get(0).getCreatedDate());
        assertEquals("2015-11-21T11:51:17Z", listResponse.getItems().get(0).getDueDate());

        assertEquals("translationJobUid2", listResponse.getItems().get(1).getTranslationJobUid());
        assertEquals("jobName2", listResponse.getItems().get(1).getJobName());
        assertEquals("PP-11111", listResponse.getItems().get(0).getJobNumber());
        assertEquals(asList("de-DE"), listResponse.getItems().get(1).getTargetLocaleIds());
        assertEquals("AWAITING_AUTHORIZATION", listResponse.getItems().get(1).getJobStatus());
        assertEquals("2015-11-24T11:51:17Z", listResponse.getItems().get(1).getCreatedDate());
        assertEquals("2015-11-23T11:51:17Z", listResponse.getItems().get(1).getDueDate());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(GET, request.getMethod());
        assertTrue(request.getPath().startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOBS_ENDPOINT).replace("{projectId}", PROJECT_ID)));
        assertTrue(request.getPath().contains("translationJobStatus=DRAFT"));
        assertTrue(request.getPath().contains("&translationJobStatus=IN_PROGRESS"));
        assertTrue(request.getPath().contains("jobName=job2"));
        assertTrue(request.getPath().contains("jobNumber=TT-123"));
        assertTrue(request.getPath().contains("translationJobUids=jobUid"));
        assertTrue(request.getPath().contains("limit=100"));
        assertTrue(request.getPath().contains("offset=10"));
        assertTrue(request.getPath().contains("sortBy=jobName"));
        assertTrue(request.getPath().contains("sortDirection=asc"));
    }

    @Test
    public void testListAccountTranslationJobs() throws InterruptedException
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.GET_ACCOUNT_TRANSLATION_JOBS_LIST_RESPONSE_BODY));

        AccountTranslationJobListCommandPTO filterBody = new AccountTranslationJobListCommandPTO(
            "job2",
            true,
            asList("project1", "project2"),
            asList("DRAFT", "IN_PROGRESS")
        );

        PagingCommandPTO pagingBody = new PagingCommandPTO(10, 100);
        SortCommandPTO sortBody = new SortCommandPTO("jobName", "asc");

        ListResponse<AccountTranslationJobListItemPTO> listResponse = translationJobsApi.listAccountTranslationJobs(
            ACCOUNT_UID,
            filterBody,
            pagingBody,
            sortBody
        );

        assertEquals(listResponse.getTotalCount(), 2);

        assertEquals("translationJobUid1", listResponse.getItems().get(0).getTranslationJobUid());
        assertEquals("jobName1", listResponse.getItems().get(0).getJobName());
        assertEquals("PP-11111", listResponse.getItems().get(0).getJobNumber());
        assertEquals(asList("fr-FR"), listResponse.getItems().get(0).getTargetLocaleIds());
        assertEquals("IN_PROGRESS", listResponse.getItems().get(0).getJobStatus());
        assertEquals("2015-11-22T11:51:17Z", listResponse.getItems().get(0).getCreatedDate());
        assertEquals("2015-11-21T11:51:17Z", listResponse.getItems().get(0).getDueDate());
        assertEquals("project1", listResponse.getItems().get(0).getProjectId());
        assertEquals(1L, (long)listResponse.getItems().get(0).getPriority());

        assertEquals("translationJobUid2", listResponse.getItems().get(1).getTranslationJobUid());
        assertEquals("jobName2", listResponse.getItems().get(1).getJobName());
        assertEquals("PP-11111", listResponse.getItems().get(0).getJobNumber());
        assertEquals(asList("de-DE"), listResponse.getItems().get(1).getTargetLocaleIds());
        assertEquals("AWAITING_AUTHORIZATION", listResponse.getItems().get(1).getJobStatus());
        assertEquals("2015-11-24T11:51:17Z", listResponse.getItems().get(1).getCreatedDate());
        assertEquals("2015-11-23T11:51:17Z", listResponse.getItems().get(1).getDueDate());
        assertEquals("project2", listResponse.getItems().get(1).getProjectId());
        assertEquals(2L, (long)listResponse.getItems().get(1).getPriority());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(GET, request.getMethod());
        assertTrue(request.getPath().startsWith(("/jobs-api/v3" + TranslationJobsApi.API_AL_JOBS_ENDPOINT).replace("{accountUid}", ACCOUNT_UID)));
        assertTrue(request.getPath().contains("jobName=job2"));
        assertTrue(request.getPath().contains("withPriority=true"));
        assertTrue(request.getPath().contains("projectIds=project1"));
        assertTrue(request.getPath().contains("&projectIds=project2"));
        assertTrue(request.getPath().contains("translationJobStatus=DRAFT"));
        assertTrue(request.getPath().contains("&translationJobStatus=IN_PROGRESS"));
        assertTrue(request.getPath().contains("limit=100"));
        assertTrue(request.getPath().contains("offset=10"));
        assertTrue(request.getPath().contains("sortBy=jobName"));
        assertTrue(request.getPath().contains("sortDirection=asc"));
    }

    @Test
    public void testAssignCustomFieldsToProject() throws InterruptedException
    {
        String requestString = "[{\"fieldUid\":\"field1\"}]";

        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.EMPTY_DATA));

        List<CustomFieldAssignmentPTO> requestBody = asList(new CustomFieldAssignmentPTO("field1"));

        translationJobsApi.assignCustomFieldsToProject(PROJECT_ID, requestBody);

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(POST, request.getMethod());
        assertEquals(requestString, request.getBody().readUtf8());
    }

    @Test
    public void testDeleteTranslationJob() throws InterruptedException
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.EMPTY_DATA));
        EmptyData responsePTO = translationJobsApi.deleteTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID);
        assertNotNull(responsePTO);

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(DELETE, request.getMethod());
        assertTrue(request.getPath().startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOBS_ENDPOINT).replace("{projectId}", PROJECT_ID)));
    }

    @Test
    public void testCloseTranslationJob() throws InterruptedException
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.EMPTY_DATA));
        EmptyData responsePTO = translationJobsApi.closeTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID);
        assertNotNull(responsePTO);

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(POST, request.getMethod());
        assertTrue(request.getPath().startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOBS_ENDPOINT).replace("{projectId}", PROJECT_ID)));
    }

    @Test
    public void testGetTranslationJobFiles() throws InterruptedException
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.GET_JOB_FILES_LIST));
        PagingCommandPTO pagingBody = new PagingCommandPTO(10, 100);
        ListResponse<FileUriPTO> response = translationJobsApi.getTranslationJobFiles(PROJECT_ID, TRANSLATION_JOB_UID, pagingBody);

        assertEquals(2, response.getTotalCount());
        assertEquals(2, response.getItems().size());
        assertEquals("/app/file1.properties", response.getItems().get(0).getUri());
        assertEquals("/app/file2.properties", response.getItems().get(1).getUri());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(GET, request.getMethod());
        assertTrue(request.getPath().startsWith(
            ("/jobs-api/v3" + TranslationJobsApi.API_JOBS_ENDPOINT).replace("{projectId}", PROJECT_ID).replace("{translationJobUid}", TRANSLATION_JOB_UID)));

        assertTrue(request.getPath().contains("limit=100"));
        assertTrue(request.getPath().contains("offset=10"));
    }

    @Test
    public void testUpdateTranslationJob() throws InterruptedException
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.GET_TRANSLATION_JOB_RESPONSE_BODY));
        String requestString = "{"
            + "\"jobName\":\"jobName\","
            + "\"description\":\"jobDescription\","
            + "\"dueDate\":\"2015-11-21T0B1:51:17Z\","
            + "\"referenceNumber\":\"referenceNumber\","
            + "\"callbackUrl\":\"https://www.callback.com/smartling/job\","
            + "\"callbackMethod\":\"GET\","
            + "\"customFields\":[{\"fieldUid\":\"field1\",\"fieldName\":\"fieldName1\",\"fieldValue\":\"value1\"},{\"fieldUid\":\"field2\",\"fieldName\":\"fieldName2\",\"fieldValue\":\"value2\"}]"
            + "}";

        TranslationJobUpdateCommandPTO requestBody = TranslationJobUpdateCommandPTO.builder()
            .jobName("jobName")
            .description("jobDescription")
            .dueDate("2015-11-21T0B1:51:17Z")
            .referenceNumber("referenceNumber")
            .callbackUrl("https://www.callback.com/smartling/job")
            .callbackMethod("GET")
            .customFields(asList(TranslationJobCustomFieldPTO.builder()
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

        TranslationJobGetResponsePTO response = translationJobsApi.updateTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, requestBody);

        assertEquals("translationJobUid", response.getTranslationJobUid());
        assertEquals("jobName", response.getJobName());
        assertEquals("PP-11111", response.getJobNumber());
        assertEquals(1, response.getTargetLocaleIds().size());
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
        assertEquals(PUT, request.getMethod());
        assertEquals(requestString, request.getBody().readUtf8());
        assertTrue(request.getPath().startsWith(
            ("/jobs-api/v3" + TranslationJobsApi.API_SINGLE_JOB_ENDPOINT).replace("{projectId}", PROJECT_ID)
                .replace("{translationJobUid}", TRANSLATION_JOB_UID)));
    }

    @Test
    public void testAddFileToTranslationJobSync() throws InterruptedException
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.COUNT_MODIFIED_STRINGS));
        String requestString = "{\"moveEnabled\":true,\"fileUri\":\"/file/app.properties\",\"targetLocaleIds\":[\"fr-FR\"]}";

        TranslationJobAddFileCommandPTO command = new TranslationJobAddFileCommandPTO("/file/app.properties", asList("fr-FR"));
        command.setMoveEnabled(true);

        StringModifiedCountResponsePTO responsePTO = translationJobsApi.addFileToTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, command);

        assertEquals(1, responsePTO.getSuccessCount());
        assertEquals(1, responsePTO.getFailCount());
        assertFalse(responsePTO.isAsync());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(POST, request.getMethod());
        assertEquals(requestString, request.getBody().readUtf8());
        assertTrue(request.getPath().startsWith(
            ("/jobs-api/v3" + TranslationJobsApi.API_JOB_ADD_FILE_ENDPOINT).replace("{projectId}", PROJECT_ID)
                .replace("{translationJobUid}", TRANSLATION_JOB_UID)));
    }

    @Test
    public void testAddFileToTranslationJobAsync() throws InterruptedException
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.ASYNC_RESPONSE));

        String requestString = "{\"moveEnabled\":true,\"fileUri\":\"/file/app.properties\",\"targetLocaleIds\":[\"fr-FR\"]}";

        TranslationJobAddFileCommandPTO command = new TranslationJobAddFileCommandPTO("/file/app.properties", asList("fr-FR"));
        command.setMoveEnabled(true);

        StringModifiedCountResponsePTO responsePTO = translationJobsApi.addFileToTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, command);

        assertEquals("http://url.com", responsePTO.getUrl());
        assertEquals("Message text", responsePTO.getMessage());
        assertTrue(responsePTO.isAsync());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(POST, request.getMethod());
        assertEquals(requestString, request.getBody().readUtf8());
        assertTrue(request.getPath().startsWith(
            ("/jobs-api/v3" + TranslationJobsApi.API_JOB_ADD_FILE_ENDPOINT).replace("{projectId}", PROJECT_ID)
                .replace("{translationJobUid}", TRANSLATION_JOB_UID)));
    }

    @Test
    public void testRemoveFileFromTranslationJobSync() throws InterruptedException
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.COUNT_MODIFIED_STRINGS));
        TranslationJobRemoveFileCommandPTO command = new TranslationJobRemoveFileCommandPTO("/file/app.properties");

        StringModifiedCountResponsePTO responsePTO = translationJobsApi.removeFileFromTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, command);

        assertNull(responsePTO.getUrl());
        assertNull(responsePTO.getMessage());
        assertEquals(1, responsePTO.getSuccessCount());
        assertEquals(1, responsePTO.getFailCount());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(POST, request.getMethod());
        assertTrue(request.getPath().startsWith(
            ("/jobs-api/v3" + TranslationJobsApi.API_JOB_REMOVE_FILE_ENDPOINT).replace("{projectId}", PROJECT_ID)
                .replace("{translationJobUid}", TRANSLATION_JOB_UID)));
    }

    @Test
    public void testRemoveFileFromTranslationJobAsync() throws InterruptedException
    {
        assignResponse(HttpStatus.SC_ACCEPTED, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.ASYNC_RESPONSE));
        TranslationJobRemoveFileCommandPTO command = new TranslationJobRemoveFileCommandPTO("/file/app.properties");

        StringModifiedCountResponsePTO responsePTO = translationJobsApi.removeFileFromTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, command);

        assertEquals("http://url.com", responsePTO.getUrl());
        assertEquals("Message text", responsePTO.getMessage());
        assertEquals(0, responsePTO.getSuccessCount());
        assertEquals(0, responsePTO.getFailCount());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(POST, request.getMethod());
        assertTrue(request.getPath().startsWith(
            ("/jobs-api/v3" + TranslationJobsApi.API_JOB_REMOVE_FILE_ENDPOINT).replace("{projectId}", PROJECT_ID)
                .replace("{translationJobUid}", TRANSLATION_JOB_UID)));
    }

    @Test
    public void testAddOrMoveStringsToTranslationJob() throws InterruptedException
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.COUNT_MODIFIED_STRINGS));

        String requestString = "{\"moveEnabled\":true,\"hashcodes\":[\"hashcode1\",\"hashcode2\"],\"targetLocaleIds\":[\"fr-FR\"]}";

        LocaleAndHashcodeListCommandPTO command = new LocaleAndHashcodeListCommandPTO(true, asList("hashcode1", "hashcode2"), asList("fr-FR"));

        StringModifiedCountResponsePTO responsePTO = translationJobsApi.addOrMoveStringsToTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, command);

        assertEquals(1, responsePTO.getSuccessCount());
        assertEquals(1, responsePTO.getFailCount());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(POST, request.getMethod());
        assertEquals(requestString, request.getBody().readUtf8());
        assertTrue(request.getPath().startsWith(
            ("/jobs-api/v3" + TranslationJobsApi.API_JOB_ADD_STRINGS_ENDPOINT).replace("{projectId}", PROJECT_ID)
                .replace("{translationJobUid}", TRANSLATION_JOB_UID)));
    }

    @Test
    public void testRemoveStringsFromTranslationJob() throws InterruptedException
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.ASYNC_RESPONSE));

        String requestString = "{\"hashcodes\":[\"hashcode1\",\"hashcode2\"],\"localeIds\":[\"fr-FR\"]}";
        HashcodesAndLocalesPTO command = new HashcodesAndLocalesPTO(asList("hashcode1", "hashcode2"), asList("fr-FR"));

        StringModifiedCountResponsePTO responsePTO = translationJobsApi.removeStringsFromTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, command);

        assertEquals("http://url.com", responsePTO.getUrl());
        assertEquals("Message text", responsePTO.getMessage());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(POST, request.getMethod());
        assertEquals(requestString, request.getBody().readUtf8());
        assertTrue(request.getPath().startsWith(
            ("/jobs-api/v3" + TranslationJobsApi.API_JOB_REMOVE_STRINGS_ENDPOINT).replace("{projectId}", PROJECT_ID)
                .replace("{translationJobUid}", TRANSLATION_JOB_UID)));

    }

    @Test
    public void testAddLocaleToTranslationJob() throws InterruptedException
    {
        given:
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.COUNT_MODIFIED_STRINGS));
        String requestString = "{\"syncContent\":true}";
        AddLocaleCommandPTO command = new AddLocaleCommandPTO(true);

        AsyncResponsePTO responsePTO = translationJobsApi.addLocaleToTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, "fr-FR", command);

        assertNull(responsePTO.getUrl());
        assertNull(responsePTO.getMessage());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(POST, request.getMethod());
        assertEquals(requestString, request.getBody().readUtf8());
        assertTrue(request.getPath().startsWith(
            ("/jobs-api/v3" + TranslationJobsApi.API_JOB_REMOVE_LOCALE_ENDPOINT).replace("{projectId}", PROJECT_ID)
                .replace("{translationJobUid}", TRANSLATION_JOB_UID).replace("{localeId}", "fr-FR")));
    }

    @Test
    public void testRemoveLocaleFromTranslationJob() throws InterruptedException
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.ASYNC_RESPONSE));

        AsyncResponsePTO responsePTO = translationJobsApi.removeLocaleFromTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, "fr-FR");

        assertEquals("http://url.com", responsePTO.getUrl());
        assertEquals("Message text", responsePTO.getMessage());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(DELETE, request.getMethod());
        assertTrue(request.getPath().startsWith(
            ("/jobs-api/v3" + TranslationJobsApi.API_JOB_REMOVE_LOCALE_ENDPOINT).replace("{projectId}", PROJECT_ID)
                .replace("{translationJobUid}", TRANSLATION_JOB_UID).replace("{localeId}", "fr-FR")));
    }

    @Test
    public void testCancelTranslationJob() throws InterruptedException
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.EMPTY_DATA));

        String requestString = "{\"reason\":\"Reason for cancel\"}";
        TranslationJobCancelCommandPTO command = new TranslationJobCancelCommandPTO("Reason for cancel");

        EmptyData responsePTO = translationJobsApi.cancelTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, command);
        assertNotNull(responsePTO);

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(POST, request.getMethod());
        assertEquals(requestString, request.getBody().readUtf8());
        assertTrue(request.getPath().startsWith(
            ("/jobs-api/v3" + TranslationJobsApi.API_JOBS_ENDPOINT).replace("{projectId}", PROJECT_ID).replace("{translationJobUid}", TRANSLATION_JOB_UID)));

    }

    @Test
    public void testGetTranslationJobFileProgress() throws InterruptedException
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.JOB_PROGRESS));
        ContentProgressReportPTO responsePTO = translationJobsApi.getTranslationJobFileProgress(PROJECT_ID, TRANSLATION_JOB_UID, "fileUri", TARGET_LOCALE_ID);

        assertEquals(1, responsePTO.getContentProgressReport().size());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(GET, request.getMethod());
        assertTrue(request.getPath().startsWith(
            ("/jobs-api/v3" + TranslationJobsApi.API_JOB_FILE_PROGRESS_ENDPOINT).replace("{projectId}", PROJECT_ID)
                .replace("{translationJobUid}", TRANSLATION_JOB_UID)));
        assertTrue(request.getPath().contains("fileUri=fileUri"));
        assertTrue(request.getPath().contains("targetLocaleId=" + TARGET_LOCALE_ID));
    }

    @Test
    public void testAuthorizeTranslationJob() throws InterruptedException
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.EMPTY_DATA));
        String requestString = "{"
            + "\"localeWorkflows\":[{"
            + "\"targetLocaleId\":\"fr-FR\","
            + "\"workflowUid\":\"w123\""
            + "}]"
            + "}";

        TranslationJobAuthorizeCommandPTO command = new TranslationJobAuthorizeCommandPTO(asList(new LocaleWorkflowCommandPTO("fr-FR", "w123")));
        EmptyData responsePTO = translationJobsApi.authorizeTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, command);

        assertNotNull(responsePTO);

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(POST, request.getMethod());
        assertEquals(requestString, request.getBody().readUtf8());
        assertTrue(request.getPath().startsWith(
            ("/jobs-api/v3" + TranslationJobsApi.API_JOB_AUTHORIZE_ENDPOINT).replace("{projectId}", PROJECT_ID)
                .replace("{translationJobUid}", TRANSLATION_JOB_UID)));
    }

    @Test
    public void testTranslationJobsSearch() throws InterruptedException
    {
        given:
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.GET_TRANSLATION_JOBS_LIST_RESPONSE_BODY));
        String requestString = "{"
            + "\"hashcodes\":[\"hashcode1\",\"hashcode2\"],"
            + "\"fileUris\":[\"/file/file1.properties\"],"
            + "\"translationJobUids\":[\"jobUid1\",\"jobUid2\"]"
            + "}";

        TranslationJobSearchCommandPTO command = new TranslationJobSearchCommandPTO(asList("hashcode1", "hashcode2"), asList("/file/file1.properties"),
            asList("jobUid1", "jobUid2"));
        ListResponse<TranslationJobListItemPTO> listResponse = translationJobsApi.translationJobsSearch(PROJECT_ID, command);

        assertEquals(2, listResponse.getTotalCount());

        assertEquals("translationJobUid1", listResponse.getItems().get(0).getTranslationJobUid());
        assertEquals("jobName1", listResponse.getItems().get(0).getJobName());
        assertEquals(asList("fr-FR"), listResponse.getItems().get(0).getTargetLocaleIds());
        assertEquals("IN_PROGRESS", listResponse.getItems().get(0).getJobStatus());
        assertEquals("2015-11-22T11:51:17Z", listResponse.getItems().get(0).getCreatedDate());
        assertEquals("2015-11-21T11:51:17Z", listResponse.getItems().get(0).getDueDate());

        assertEquals("translationJobUid2", listResponse.getItems().get(1).getTranslationJobUid());
        assertEquals("jobName2", listResponse.getItems().get(1).getJobName());
        assertEquals(asList("de-DE"), listResponse.getItems().get(1).getTargetLocaleIds());
        assertEquals("AWAITING_AUTHORIZATION", listResponse.getItems().get(1).getJobStatus());
        assertEquals("2015-11-24T11:51:17Z", listResponse.getItems().get(1).getCreatedDate());
        assertEquals("2015-11-23T11:51:17Z", listResponse.getItems().get(1).getDueDate());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(POST, request.getMethod());
        assertEquals(requestString, request.getBody().readUtf8());
        assertTrue(request.getPath().startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOBS_SEARCH_ENDPOINT).replace("{projectId}", PROJECT_ID)));
    }

    @Test
    public void testGetTranslationJobProgress() throws InterruptedException
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.GET_TRANSLATION_JOB_PROGRESS_RESPONSE_BODY));

        ContentProgressReportPTO response = translationJobsApi.getTranslationJobProgress(PROJECT_ID, TRANSLATION_JOB_UID, TARGET_LOCALE_ID);

        assertEquals(4358, response.getProgress().getTotalWordCount());
        assertEquals(1, response.getProgress().getPercentComplete());

        assertEquals("es-LA", response.getContentProgressReport().get(0).getTargetLocaleId());
        assertEquals("es-ES", response.getContentProgressReport().get(1).getTargetLocaleId());

        assertEquals("Spanish (Latin America)", response.getContentProgressReport().get(0).getTargetLocaleDescription());
        assertEquals("Spanish (Spain)", response.getContentProgressReport().get(1).getTargetLocaleDescription());

        LocaleContentProgressReportPTO report1 = response.getContentProgressReport().get(0);

        List<WorkflowProgressReportPTO> workflowReport1 = report1.getWorkflowProgressReportList();
        assertEquals("84daac787eb5", workflowReport1.get(0).getWorkflowUid());
        List<WorkflowStepSummaryReportItemPTO> workflow1Steps = workflowReport1.get(0).getWorkflowStepSummaryReportItemList();
        assertEquals("5963a9155d2c", workflow1Steps.get(0).getWorkflowStepUid());
        assertEquals("596348abe74a", workflow1Steps.get(1).getWorkflowStepUid());
        assertEquals("Translation", workflow1Steps.get(0).getWorkflowStepName());
        assertEquals("Published", workflow1Steps.get(1).getWorkflowStepName());
        assertEquals("TRANSLATION", workflow1Steps.get(0).getWorkflowStepType());
        assertEquals("PUBLISH", workflow1Steps.get(1).getWorkflowStepType());
        assertEquals(2003, workflow1Steps.get(0).getWordCount());
        assertEquals(39, workflow1Steps.get(1).getWordCount());
        assertEquals(62, workflow1Steps.get(0).getStringCount());
        assertEquals(6, workflow1Steps.get(1).getStringCount());

        assertEquals(7, report1.getSummaryReport().get(0).getStringCount());
        assertEquals(62, report1.getSummaryReport().get(1).getStringCount());
        assertEquals(6, report1.getSummaryReport().get(2).getStringCount());
        assertEquals(105, report1.getSummaryReport().get(0).getWordCount());
        assertEquals(2003, report1.getSummaryReport().get(1).getWordCount());
        assertEquals(39, report1.getSummaryReport().get(2).getWordCount());
        assertEquals("AWAITING_AUTHORIZATION", report1.getSummaryReport().get(0).getWorkflowStepType());
        assertEquals("TRANSLATION", report1.getSummaryReport().get(1).getWorkflowStepType());
        assertEquals("PUBLISH", report1.getSummaryReport().get(2).getWorkflowStepType());

        assertEquals(7, report1.getUnauthorizedProgressReport().getStringCount());
        assertEquals(105, report1.getUnauthorizedProgressReport().getWordCount());

        assertEquals(2147, report1.getProgress().getTotalWordCount());
        assertEquals(1, report1.getProgress().getPercentComplete());

        LocaleContentProgressReportPTO report2 = response.getContentProgressReport().get(1);

        assertEquals(84, report2.getSummaryReport().get(0).getStringCount());
        assertEquals(7, report2.getSummaryReport().get(1).getStringCount());
        assertEquals(2176, report2.getSummaryReport().get(0).getWordCount());
        assertEquals(34, report2.getSummaryReport().get(1).getWordCount());
        assertEquals("TRANSLATION", report2.getSummaryReport().get(0).getWorkflowStepType());
        assertEquals("PUBLISH", report2.getSummaryReport().get(1).getWorkflowStepType());

        assertEquals(0, report2.getUnauthorizedProgressReport().getStringCount());
        assertEquals(0, report2.getUnauthorizedProgressReport().getWordCount());

        assertEquals(2211, report2.getProgress().getTotalWordCount());
        assertEquals(1, report2.getProgress().getPercentComplete());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(GET, request.getMethod());
        assertTrue(request.getPath().startsWith(("/jobs-api/v3" + TranslationJobsApi.API_JOB_PROGRESS_ENDPOINT).replace("{projectId}", PROJECT_ID)
            .replace("{translationJobUid}", TRANSLATION_JOB_UID)));
        assertTrue(request.getPath().contains("targetLocaleId=" + TARGET_LOCALE_ID));
    }


    @Test
    public void testFindTranslationJobsByLocalesAndHashcodes() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.FIND_JOB_BY_LOCALES_AND_HASHCODES_RESPONSE_BODY));

        TranslationJobFindByLocalesAndHashcodesCommandPTO command = new TranslationJobFindByLocalesAndHashcodesCommandPTO(Arrays.asList("en-US", "fr-FR"),
            Arrays.asList("h1", "h2", "h3"));

        ListResponse<TranslationJobFoundByStringsAndLocalesResponsePTO> response = translationJobsApi
            .findTranslationJobsByLocalesAndHashcodes(PROJECT_ID, command);

        assertEquals(3, response.getTotalCount());
        assertEquals(2, response.getItems().size());

        assertEquals("translationJobUid1", response.getItems().get(0).getTranslationJobUid());
        assertEquals("2015-11-21T11:51:17Z", response.getItems().get(0).getDueDate());
        assertEquals("jobName1", response.getItems().get(0).getJobName());
        assertEquals(2, response.getItems().get(0).getHashcodesByLocale().size());
        assertEquals("fr-FR", response.getItems().get(0).getHashcodesByLocale().get(0).getLocaleId());
        assertEquals("hashcode1", response.getItems().get(0).getHashcodesByLocale().get(0).getHashcodes().get(0));
        assertEquals("hashcode3", response.getItems().get(0).getHashcodesByLocale().get(0).getHashcodes().get(1));
        assertEquals("de-DE", response.getItems().get(0).getHashcodesByLocale().get(1).getLocaleId());
        assertEquals("hashcode4", response.getItems().get(0).getHashcodesByLocale().get(1).getHashcodes().get(0));
        assertEquals("hashcode3", response.getItems().get(0).getHashcodesByLocale().get(1).getHashcodes().get(1));

        assertEquals("translationJobUid2", response.getItems().get(1).getTranslationJobUid());
        assertEquals("2015-11-23T11:51:17Z", response.getItems().get(1).getDueDate());
        assertEquals("jobName2", response.getItems().get(1).getJobName());
        assertEquals(2, response.getItems().get(1).getHashcodesByLocale().size());
        assertEquals("es-ES", response.getItems().get(1).getHashcodesByLocale().get(0).getLocaleId());
        assertEquals("hashcode5", response.getItems().get(1).getHashcodesByLocale().get(0).getHashcodes().get(0));
        assertEquals("hashcode6", response.getItems().get(1).getHashcodesByLocale().get(0).getHashcodes().get(1));
        assertEquals("it-IT", response.getItems().get(1).getHashcodesByLocale().get(1).getLocaleId());
        assertEquals("hashcode7", response.getItems().get(1).getHashcodesByLocale().get(1).getHashcodes().get(0));
        assertEquals("hashcode8", response.getItems().get(1).getHashcodesByLocale().get(1).getHashcodes().get(1));

        final RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertTrue(request.getPath()
            .contains(TranslationJobsApi.API_JOB_FIND_BY_LOCALES_AND_HASHCODES_ENDPOINT.replace("{projectId}", PROJECT_ID)));
    }

    @Test
    public void testGetStringsForTranslationJob() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, SampleApiResponses.FIND_STRINGS_FOR_JOB_RESPONSE_BODY));

        ListResponse<LocaleHashcodePairPTO> response = translationJobsApi
            .getStringsForTranslationJob(PROJECT_ID, TRANSLATION_JOB_UID, null, new PagingCommandPTO(0, 100));

        assertEquals(2, response.getTotalCount());
        assertEquals(2, response.getItems().size());

        assertEquals("hashcode1", response.getItems().get(0).getHashcode());
        assertEquals("aa-AA", response.getItems().get(0).getTargetLocaleId());

        assertEquals("hashcode2", response.getItems().get(1).getHashcode());
        assertEquals("aa-AA", response.getItems().get(1).getTargetLocaleId());

        final RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertTrue(request.getPath()
            .contains(TranslationJobsApi.API_JOB_CONTENTS_ENDPOINT.replace("{projectId}", PROJECT_ID).replace("{translationJobUid}", TRANSLATION_JOB_UID)));
        assertTrue(request.getPath().contains("limit=100"));
        assertTrue(request.getPath().contains("offset=0"));
    }
}
