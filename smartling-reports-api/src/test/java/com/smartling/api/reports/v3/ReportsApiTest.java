package com.smartling.api.reports.v3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.smartling.api.reports.v3.pto.WordCountReportCommandPTO;
import com.smartling.api.reports.v3.pto.WordCountResponsePTO;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.v2.response.ListResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReportsApiTest
{
    private final static String WCR_SUCCESS_RESPONSE =
        "{\n"
            + "  \"response\": {\n"
            + "    \"code\": \"SUCCESS\",\n"
            + "    \"data\": {\n"
            + "      \"resultsTruncated\": false,\n"
            + "      \"items\": [\n"
            + "        {\n"
            + "          \"accountUid\": null,\n"
            + "          \"accountName\": \"Life360\",\n"
            + "          \"projectId\": null,\n"
            + "          \"projectName\": \"Life360 - Mobile (formerly TMS)\",\n"
            + "          \"targetLocaleId\": null,\n"
            + "          \"targetLocale\": \"Russian (Russia) [ru-RU]\",\n"
            + "          \"jobUid\": null,\n"
            + "          \"jobName\": null,\n"
            + "          \"jobReferenceNumber\": null,\n"
            + "          \"translationResourceUid\": null,\n"
            + "          \"translationResourceName\": null,\n"
            + "          \"agencyUid\": null,\n"
            + "          \"agencyName\": null,\n"
            + "          \"workflowStepType\": \"Translation\",\n"
            + "          \"workflowStepUid\": null,\n"
            + "          \"workflowStepName\": null,\n"
            + "          \"fuzzyProfileName\": null,\n"
            + "          \"fuzzyTier\": null,\n"
            + "          \"wordCount\": 16,\n"
            + "          \"weightedWordCount\": 16\n"
            + "        },\n"
            + "        {\n"
            + "          \"accountUid\": null,\n"
            + "          \"accountName\": \"Life360\",\n"
            + "          \"projectId\": null,\n"
            + "          \"projectName\": \"Life360 - Mobile (formerly TMS)\",\n"
            + "          \"targetLocaleId\": null,\n"
            + "          \"targetLocale\": \"Russian (Russia) [ru-RU]\",\n"
            + "          \"jobUid\": null,\n"
            + "          \"jobName\": null,\n"
            + "          \"jobReferenceNumber\": null,\n"
            + "          \"translationResourceUid\": null,\n"
            + "          \"translationResourceName\": null,\n"
            + "          \"agencyUid\": null,\n"
            + "          \"agencyName\": null,\n"
            + "          \"workflowStepType\": \"Edit\",\n"
            + "          \"workflowStepUid\": null,\n"
            + "          \"workflowStepName\": null,\n"
            + "          \"fuzzyProfileName\": null,\n"
            + "          \"fuzzyTier\": null,\n"
            + "          \"wordCount\": 69,\n"
            + "          \"weightedWordCount\": 69\n"
            + "        },\n"
            + "        {\n"
            + "          \"accountUid\": null,\n"
            + "          \"accountName\": \"Life360\",\n"
            + "          \"projectId\": null,\n"
            + "          \"projectName\": \"Life360 - Mobile (formerly TMS)\",\n"
            + "          \"targetLocaleId\": null,\n"
            + "          \"targetLocale\": \"French (France) [fr-FR]\",\n"
            + "          \"jobUid\": null,\n"
            + "          \"jobName\": null,\n"
            + "          \"jobReferenceNumber\": null,\n"
            + "          \"translationResourceUid\": null,\n"
            + "          \"translationResourceName\": null,\n"
            + "          \"agencyUid\": \"907c153f\",\n"
            + "          \"agencyName\": \"Anja Jones Translation\",\n"
            + "          \"workflowStepType\": \"Translation\",\n"
            + "          \"workflowStepUid\": null,\n"
            + "          \"workflowStepName\": null,\n"
            + "          \"fuzzyProfileName\": null,\n"
            + "          \"fuzzyTier\": null,\n"
            + "          \"wordCount\": 16,\n"
            + "          \"weightedWordCount\": 16\n"
            + "        }\n"
            + "      ]\n"
            + "    }\n"
            + "  }\n"
            + "}";

    private MockWebServer mockWebServer;
    private ReportsApi reportsApi;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception
    {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        objectMapper = new ObjectMapper();

        final ReportsApiFactory factory = new ReportsApiFactory();
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("foo");
        ClientConfiguration config = DefaultClientConfiguration.builder().baseUrl(mockWebServer.url("/").url()).build();

        reportsApi = factory.buildApi(tokenFilter, config);
    }

    @After
    public void tearDown() throws Exception
    {
        mockWebServer.shutdown();
    }

    private void assignResponse(int httpStatusCode, String body)
    {
        MockResponse response = new MockResponse()
            .setResponseCode(httpStatusCode)
            .setHeader(HttpHeaders.CONTENT_LENGTH, body.length())
            .setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
            .setBody(body);

        mockWebServer.enqueue(response);
    }

    @Test
    public void testGetApiClass()
    {
        assertEquals(ReportsApi.class, new ReportsApiFactory().getApiClass());
    }

    @Test
    public void testGetWordCountReport() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, WCR_SUCCESS_RESPONSE);

        ListResponse<WordCountResponsePTO> results = reportsApi.wordCountReport(createCommand());

        assertEquals(3, results.getItems().size());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertTrue(request.getPath().contains("/reports-api/v3/word-count"));

        assertTrue(request.getPath().contains("accountUid=a71308ec"));
        assertTrue(request.getPath().contains("startDate=2021-12-01"));
        assertTrue(request.getPath().contains("endDate=2021-12-02"));

        assertTrue(request.getPath().contains("projectIds=e240a7892"));
        assertTrue(request.getPath().contains("projectIds=0d96d0fcf"));

        assertTrue(request.getPath().contains("targetLocaleIds=ru-RU"));
        assertTrue(request.getPath().contains("targetLocaleIds=fr-FR"));

        assertTrue(request.getPath().contains("jobUids=jobUid1"));
        assertTrue(request.getPath().contains("userUids=userUid1"));
        assertTrue(request.getPath().contains("agencyUid=agencyUid1"));

        assertTrue(request.getPath().contains("includeJob=false"));
        assertTrue(request.getPath().contains("includeJobReferenceNumber=false"));
        assertTrue(request.getPath().contains("includeFuzzyMatchProfile=false"));
        assertTrue(request.getPath().contains("includeWorkflowStep=false"));
        assertTrue(request.getPath().contains("includeTranslationResource=false"));

        assertTrue(request.getPath().contains("fields=field1"));
        assertTrue(request.getPath().contains("fields=field2"));

        assertTrue(request.getPath().contains("limit=500"));
        assertTrue(request.getPath().contains("offset=0"));
    }

    @Test
    public void testDownloadWordCountReport() throws Exception
    {
        assignResponse(HttpStatus.SC_OK,"test");

        try(InputStream stream = reportsApi.downloadWordCountReport(createCommand()))
        {
            String content = IOUtils.toString(stream, StandardCharsets.UTF_8);
            assertThat(content, is("test"));
        }

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertTrue(request.getPath().contains("/reports-api/v3/word-count/csv"));
    }

    private WordCountReportCommandPTO createCommand()
    {
        return WordCountReportCommandPTO.builder()
            .startDate("2021-12-01")
            .endDate("2021-12-02")
            .accountUid("a71308ec")
            .projectId("e240a7892")
            .projectId("0d96d0fcf")
            .targetLocaleId("ru-RU")
            .targetLocaleId("fr-FR")
            .jobUid("jobUid1")
            .userUid("userUid1")
            .agencyUid("agencyUid1")
            .includeJob(false)
            .includeJobReferenceNumber(false)
            .includeFuzzyMatchProfile(false)
            .includeWorkflowStep(false)
            .includeTranslationResource(false)
            .fields(Lists.newArrayList("field1", "field2"))
            .limit(500)
            .build();
    }
}

