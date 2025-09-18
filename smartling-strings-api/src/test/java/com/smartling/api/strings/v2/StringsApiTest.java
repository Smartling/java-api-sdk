package com.smartling.api.strings.v2;

import com.smartling.api.strings.v2.pto.AsyncStatusResponsePTO;
import com.smartling.api.strings.v2.pto.CreatedStringsListPTO;
import com.smartling.api.strings.v2.pto.GetSourceStringsCommandPTO;
import com.smartling.api.strings.v2.pto.SourceStringListPTO;
import com.smartling.api.strings.v2.pto.StringsCreationPTO;
import com.smartling.api.strings.v2.pto.TranslationsCommandPTO;
import com.smartling.api.strings.v2.pto.TranslationsPTO;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.v2.response.ListResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class StringsApiTest
{
    private static final String PROJECT_UID = "project_uid";
    private static final String CREATE_STRINGS =
        "{"
        + "  \"response\": {"
        + "    \"code\": \"SUCCESS\","
        + "    \"data\": {"
        + "      \"items\": ["
        + "        {"
        + "          \"hashcode\": \"bd603147d945h3ec74d6874422ebe4e0\","
        + "          \"overWritten\": \"true\","
        + "          \"parsedStringText\": \"Search results for {0}:\","
        + "          \"stringText\": \"Search results for [city]:\","
        + "          \"variant\": \"variant value\""
        + "        }"
        + "      ],"
        + "      \"processUid\": \"d6414f8c-4gdf-4415-9a69-f8e7902ca9ec\","
        + "      \"stringCount\": 1,"
        + "      \"wordCount\": 4"
        + "    }"
        + "  }"
        + "}";

    private static final String ASYNC_PROCESSES =
        "{"
        + "  \"response\": {"
        + "    \"code\": \"SUCCESS\","
        + "    \"data\": {"
        + "      \"createdDate\": \"string\","
        + "      \"modifiedDate\": \"string\","
        + "      \"processState\": \"OPEN\","
        + "      \"processStatistics\": {"
        + "        \"errored\": 1,"
        + "        \"processed\": 2,"
        + "        \"requested\": 3,"
        + "        \"skipped\": 4"
        + "      },"
        + "      \"processUid\": \"string\""
        + "    }"
        + "  }"
        + "}";

    private static final String SOURCE_STRINGS =
        "{"
        + "  \"response\": {"
        + "    \"code\": \"SUCCESS\","
        + "    \"data\": {"
        + "      \"items\": ["
        + "        {"
        + "          \"hashcode\": \"string\","
        + "          \"keys\": ["
        + "            {"
        + "              \"fileUri\": \"string\","
        + "              \"key\": \"string\""
        + "            }"
        + "          ],"
        + "          \"parsedStringText\": \"string\","
        + "          \"stringText\": \"string\","
        + "          \"stringVariant\": \"string\""
        + "        }"
        + "      ],"
        + "      \"totalCount\": 0"
        + "    }"
        + "  }"
        +  "}";

    private static final String TRANSLATIONS =
        "{"
        + "  \"response\": {"
        + "    \"code\": \"SUCCESS\","
        + "    \"data\": {"
        + "      \"items\": ["
        + "        {"
        + "          \"hashcode\": \"string\","
        + "          \"keys\": ["
        + "            {"
        + "              \"fileUri\": \"string\","
        + "              \"key\": \"string\""
        + "            }"
        + "          ],"
        + "          \"parsedStringText\": \"string\","
        + "          \"stringText\": \"string\","
        + "          \"targetlocaleId\": \"string\","
        + "          \"translations\": ["
        + "            {"
        + "              \"modifiedDate\": \"string\","
        + "              \"pluralForm\": \"string\","
        + "              \"translation\": \"string\""
        + "            }"
        + "          ],"
        + "          \"variant\": \"string\","
        + "          \"workflowStepUid\": \"142473a4-f835-4548-8cd3-dff5018abdcb\""
        + "        }"
        + "      ],"
        + "      \"totalCount\": 0"
        + "    }"
        + "  }"
        + "}";

    private MockWebServer mockWebServer;
    private StringsApi stringsApi;

    @Before
    public void setUp() throws Exception
    {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        ClientConfiguration config = DefaultClientConfiguration.builder().baseUrl(mockWebServer.url("/").url()).build();
        stringsApi = new StringsApiFactory().buildApi(new BearerAuthStaticTokenFilter("foo"), config);
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
    public void testGetApiClass() throws Exception
    {
        assertEquals(StringsApi.class, new StringsApiFactory().getApiClass());
    }

    @Test
    public void testCreateStrings() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, CREATE_STRINGS);

        CreatedStringsListPTO createdStringsListPTO = stringsApi.createStrings(PROJECT_UID, new StringsCreationPTO());
        assertNotNull(createdStringsListPTO);
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertTrue(request.getPath().contains("/projects/" + PROJECT_UID));
    }

    @Test
    public void testCheckStatus() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, ASYNC_PROCESSES);

        AsyncStatusResponsePTO asyncProcesses = stringsApi.checkStatus(PROJECT_UID, "process-uid");
        assertNotNull(asyncProcesses);
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertTrue(request.getPath().contains("/projects/" + PROJECT_UID + "/processes/process-uid"));
    }

    @Test
    public void testGetSourceStrings() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, SOURCE_STRINGS);

        SourceStringListPTO sourceStrings = stringsApi.getSourceStrings(PROJECT_UID, new GetSourceStringsCommandPTO());
        assertNotNull(sourceStrings);
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertTrue(request.getPath().contains("/projects/" + PROJECT_UID + "/source-strings"));
    }

    @Test
    public void getSourceStringsPost() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, SOURCE_STRINGS);

        SourceStringListPTO sourceStrings = stringsApi.getSourceStringsPost(PROJECT_UID, new GetSourceStringsCommandPTO());
        assertNotNull(sourceStrings);
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertTrue(request.getPath().contains("/projects/" + PROJECT_UID + "/source-strings"));
    }

    @Test
    public void testGetTranslations() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, TRANSLATIONS);

        ListResponse<TranslationsPTO> translations = stringsApi.getTranslations(PROJECT_UID, new TranslationsCommandPTO());
        assertNotNull(translations);
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertTrue(request.getPath().contains("/projects/" + PROJECT_UID + "/translations"));
    }

    @Test
    public void testGetTranslationsPost() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, TRANSLATIONS);

        ListResponse<TranslationsPTO> translations = stringsApi.getTranslationsPost(PROJECT_UID, new TranslationsCommandPTO());
        assertNotNull(translations);
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertTrue(request.getPath().contains("/projects/" + PROJECT_UID + "/translations"));
    }
}
