package com.smartling.glossary.v3;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.v2.response.EmptyData;
import com.smartling.api.v2.response.ListResponse;
import com.smartling.glossary.v3.pto.AsyncOperationResponsePTO;
import com.smartling.glossary.v3.pto.GlossariesArchiveUnarchivedResponsePTO;
import com.smartling.glossary.v3.pto.GlossaryEntriesCountsResponsePTO;
import com.smartling.glossary.v3.pto.GlossaryResponsePTO;
import com.smartling.glossary.v3.pto.GlossarySearchResponsePTO;
import com.smartling.glossary.v3.pto.authorize.EntriesAuthorizationCommandPTO;
import com.smartling.glossary.v3.pto.authorize.LocaleWorkflowPairCommandPTO;
import com.smartling.glossary.v3.pto.command.GetGlossariesByFilterCommandPTO;
import com.smartling.glossary.v3.pto.command.GetGlossariesWithEntriesCountByFilterCommandPTO;
import com.smartling.glossary.v3.pto.command.GlossariesArchiveUnarchiveCommandPTO;
import com.smartling.glossary.v3.pto.command.GlossaryCommandPTO;
import com.smartling.glossary.v3.pto.entry.command.filter.GetGlossaryEntriesByFilterCommandPTO;
import com.smartling.glossary.v3.pto.ie.GlossaryImportPTO;
import com.smartling.glossary.v3.pto.ie.GlossaryImportResponsePTO;
import com.smartling.glossary.v3.pto.ie.command.GlossaryEntriesExportCommandPTO;
import com.smartling.glossary.v3.pto.ie.command.GlossaryImportCommandPTO;
import com.smartling.glossary.v3.pto.label.GlossaryLabelCommandPTO;
import com.smartling.glossary.v3.pto.label.GlossaryLabelPTO;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GlossaryApiTest {

    private final static String FILE_URI = "glossary.csv";
    private final static String FILE_MEDIA_TYPE = "text/csv";

    private MockWebServer mockWebServer;

    private GlossaryApi glossaryApi;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private static final String GLOSSARY_COMMON_RESPONSE = "{\n"
        + "\t\"response\": {\n"
        + "\t\t\"code\": \"SUCCESS\",\n"
        + "\t\t\"data\": {\n"
        + "\n"
        + "\t\t\t\"glossaryUid\": \"bad90990-4be1-4570-81da-b719cddd5352\",\n"
        + "\t\t\t\"accountUid\": \"56a0b220\",\n"
        + "\t\t\t\"glossaryName\": \"fallback-test\",\n"
        + "\t\t\t\"description\": \"aaaa\",\n"
        + "\t\t\t\"verificationMode\": false,\n"
        + "\t\t\t\"archived\": true,\n"
        + "\t\t\t\"createdByUserUid\": \"671b520051b2\",\n"
        + "\t\t\t\"modifiedByUserUid\": \"671b520051b2\",\n"
        + "\t\t\t\"createdDate\": \"2023-03-30T10:37:54Z\",\n"
        + "\t\t\t\"modifiedDate\": \"2023-04-21T11:12:55Z\",\n"
        + "\t\t\t\"localeIds\": [\"uk-UA\", \"en-US\", \"en\"],\n"
        + "\t\t\t\"fallbackLocales\": [{\n"
        + "\t\t\t\t\"fallbackLocaleId\": \"en-US\",\n"
        + "\t\t\t\t\"localeIds\": [\"en\"]\n"
        + "\t\t\t}],\n"
        + "\t\t\t\"entriesCount\": null\n"
        + "\n"
        + "\t\t}\n"
        + "\t}\n"
        + "}";

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final GlossaryApiFactory factory = new GlossaryApiFactory();
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("foo");
        final ClientConfiguration config = DefaultClientConfiguration.builder().baseUrl(mockWebServer.url("/").url()).build();

        glossaryApi = factory.buildApi(tokenFilter, config);
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    private void assignResponse(final int httpStatusCode, final String body) {
        final MockResponse response = new MockResponse()
            .setResponseCode(httpStatusCode)
            .setHeader(HttpHeaders.CONTENT_LENGTH, body.length())
            .setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
            .setBody(body);

        mockWebServer.enqueue(response);
    }

    @Test
    public void testReadGlossary() throws Exception {

        assignResponse(200, GLOSSARY_COMMON_RESPONSE);

        GlossaryResponsePTO glossaryResponsePTO = glossaryApi.readGlossary("56a0b220", "bad90990-4be1-4570-81da-b719cddd5352");

        assertEquals("bad90990-4be1-4570-81da-b719cddd5352", glossaryResponsePTO.getGlossaryUid());
        assertEquals("56a0b220", glossaryResponsePTO.getAccountUid());
        assertEquals("aaaa", glossaryResponsePTO.getDescription());
        assertNotNull(glossaryResponsePTO.getLocaleIds());
        assertNotNull(glossaryResponsePTO.getFallbackLocales());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());

    }

    @Test
    public void createGlossary() throws Exception {

        assignResponse(200, GLOSSARY_COMMON_RESPONSE);

        GlossaryResponsePTO glossaryResponsePTO = glossaryApi
            .createGlossary("56a0b220", GlossaryCommandPTO.builder().glossaryName("fallback-test").build());

        assertEquals("bad90990-4be1-4570-81da-b719cddd5352", glossaryResponsePTO.getGlossaryUid());
        assertEquals("56a0b220", glossaryResponsePTO.getAccountUid());
        assertEquals("aaaa", glossaryResponsePTO.getDescription());
        assertNotNull(glossaryResponsePTO.getLocaleIds());
        assertNotNull(glossaryResponsePTO.getFallbackLocales());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());

    }

    @Test
    public void updateGlossary() throws Exception {

        assignResponse(200, GLOSSARY_COMMON_RESPONSE);

        GlossaryResponsePTO glossaryResponsePTO = glossaryApi
            .updateGlossary("56a0b220", "bad90990-4be1-4570-81da-b719cddd5352", GlossaryCommandPTO.builder().glossaryName("fallback-test").build());

        assertEquals("bad90990-4be1-4570-81da-b719cddd5352", glossaryResponsePTO.getGlossaryUid());
        assertEquals("56a0b220", glossaryResponsePTO.getAccountUid());
        assertEquals("aaaa", glossaryResponsePTO.getDescription());
        assertNotNull(glossaryResponsePTO.getLocaleIds());
        assertNotNull(glossaryResponsePTO.getFallbackLocales());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("PUT", recordedRequest.getMethod());

    }

    @Test
    public void archiveGlossary() throws Exception {

        final String response = "{\"response\":{\"code\":\"SUCCESS\",\"data\":{\"glossaryUids\":[\"bad90990-4be1-4570-81da-b719cddd5352\"]}}}";
        assignResponse(200, response);

        GlossariesArchiveUnarchivedResponsePTO glossaryResponsePTO = glossaryApi
            .archiveGlossaries("56a0b220", GlossariesArchiveUnarchiveCommandPTO.builder().
                glossaryUids(Collections.singletonList("bad90990-4be1-4570-81da-b719cddd5352")).build());

        assertNotNull(glossaryResponsePTO);
        assertNotNull(glossaryResponsePTO.getGlossaryUids());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());

    }

    @Test
    public void unarchiveGlossary() throws Exception {

        final String response = "{\"response\":{\"code\":\"SUCCESS\",\"data\":{\"glossaryUids\":[\"bad90990-4be1-4570-81da-b719cddd5352\"]}}}";
        assignResponse(200, response);

        GlossariesArchiveUnarchivedResponsePTO glossaryResponsePTO = glossaryApi
            .restoreGlossaries("56a0b220", GlossariesArchiveUnarchiveCommandPTO.builder().
                glossaryUids(Collections.singletonList("bad90990-4be1-4570-81da-b719cddd5352")).build());

        assertNotNull(glossaryResponsePTO);
        assertNotNull(glossaryResponsePTO.getGlossaryUids());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());

    }

    @Test
    public void searchCountGlossaries() throws Exception {

        final String response = "{\n"
            + "\t\"response\": {\n"
            + "\t\t\"code\": \"SUCCESS\",\n"
            + "\t\t\"data\": {\n"
            + "\t\t\t\"totalCount\": 9,\n"
            + "\t\t\t\"items\": [{\n"
            + "\t\t\t\t\"glossaryUid\": \"3abf84a5-f911-4603-88e8-a3b089ccb4f7\",\n"
            + "\t\t\t\t\"entriesCount\": 0,\n"
            + "\t\t\t\t\"blocklistsCount\": 0\n"
            + "\t\t\t}, {\n"
            + "\t\t\t\t\"glossaryUid\": \"3d65d87c-58b2-4454-9d5a-5eb1f36ff636\",\n"
            + "\t\t\t\t\"entriesCount\": 3,\n"
            + "\t\t\t\t\"blocklistsCount\": 0\n"
            + "\t\t\t}, {\n"
            + "\t\t\t\t\"glossaryUid\": \"4900c21b-f368-4849-827c-9c2fe92f1d29\",\n"
            + "\t\t\t\t\"entriesCount\": 3,\n"
            + "\t\t\t\t\"blocklistsCount\": 0\n"
            + "\t\t\t}, {\n"
            + "\t\t\t\t\"glossaryUid\": \"56d3857c-eaeb-4dcb-87cf-ed6b7d1aeb75\",\n"
            + "\t\t\t\t\"entriesCount\": 0,\n"
            + "\t\t\t\t\"blocklistsCount\": 0\n"
            + "\t\t\t}, {\n"
            + "\t\t\t\t\"glossaryUid\": \"58966072-6569-4c22-930c-de906b7fb18d\",\n"
            + "\t\t\t\t\"entriesCount\": 5,\n"
            + "\t\t\t\t\"blocklistsCount\": 1\n"
            + "\t\t\t}, {\n"
            + "\t\t\t\t\"glossaryUid\": \"ac94b83c-68a2-4c29-83e0-dcf55f31504d\",\n"
            + "\t\t\t\t\"entriesCount\": 1,\n"
            + "\t\t\t\t\"blocklistsCount\": 0\n"
            + "\t\t\t}, {\n"
            + "\t\t\t\t\"glossaryUid\": \"afbb6b33-c099-4678-810c-e0b502978f47\",\n"
            + "\t\t\t\t\"entriesCount\": 9,\n"
            + "\t\t\t\t\"blocklistsCount\": 1\n"
            + "\t\t\t}, {\n"
            + "\t\t\t\t\"glossaryUid\": \"bad90990-4be1-4570-81da-b719cddd5352\",\n"
            + "\t\t\t\t\"entriesCount\": 2,\n"
            + "\t\t\t\t\"blocklistsCount\": 0\n"
            + "\t\t\t}, {\n"
            + "\t\t\t\t\"glossaryUid\": \"c82efa14-7013-47f8-9b18-5b9a7ded48f2\",\n"
            + "\t\t\t\t\"entriesCount\": 10,\n"
            + "\t\t\t\t\"blocklistsCount\": 0\n"
            + "\t\t\t}]\n"
            + "\t\t}\n"
            + "\t}\n"
            + "}";
        assignResponse(200, response);

        ListResponse<GlossaryEntriesCountsResponsePTO> glossaryResponsePTO = glossaryApi.searchGlossariesWithEntriesCounts("56a0b220",
            GetGlossariesByFilterCommandPTO.builder().glossaryState("BOTH").build()
        );

        assertNotNull(glossaryResponsePTO);
        assertEquals(glossaryResponsePTO.getTotalCount(), 9);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());

    }

    @Test
    public void searchGlossaries() throws Exception {

        final String response = "{\n"
            + "\t\"response\": {\n"
            + "\t\t\"code\": \"SUCCESS\",\n"
            + "\t\t\"data\": {\n"
            + "\t\t\t\"totalCount\": 1,\n"
            + "\t\t\t\"items\": [{\n"
            + "\t\t\t\t\"glossaryUid\": \"bad90990-4be1-4570-81da-b719cddd5352\",\n"
            + "\t\t\t\t\"accountUid\": \"56a0b220\",\n"
            + "\t\t\t\t\"glossaryName\": \"fallback-test\",\n"
            + "\t\t\t\t\"description\": \"aaaa\",\n"
            + "\t\t\t\t\"verificationMode\": false,\n"
            + "\t\t\t\t\"archived\": true,\n"
            + "\t\t\t\t\"createdByUserUid\": \"671b520051b2\",\n"
            + "\t\t\t\t\"modifiedByUserUid\": \"671b520051b2\",\n"
            + "\t\t\t\t\"createdDate\": \"2023-03-30T10:37:54Z\",\n"
            + "\t\t\t\t\"modifiedDate\": \"2023-04-21T11:12:55Z\",\n"
            + "\t\t\t\t\"localeIds\": [\"uk-UA\", \"en-US\", \"en\"],\n"
            + "\t\t\t\t\"fallbackLocales\": [{\n"
            + "\t\t\t\t\t\"fallbackLocaleId\": \"en-US\",\n"
            + "\t\t\t\t\t\"localeIds\": [\"en\"]\n"
            + "\t\t\t\t}],\n"
            + "\t\t\t\t\"entriesCount\": null\n"
            + "\t\t\t}]\n"
            + "\t\t}\n"
            + "\t}\n"
            + "}";
        assignResponse(200, response);

        ListResponse<GlossarySearchResponsePTO> glossaryResponsePTO = glossaryApi.searchGlossaries("56a0b220",
            GetGlossariesWithEntriesCountByFilterCommandPTO.builder().query("fall").glossaryState("BOTH").build()
        );

        assertNotNull(glossaryResponsePTO);
        assertEquals(glossaryResponsePTO.getTotalCount(), 1);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());

    }

    @Test
    public void testInitializeImport() throws InterruptedException {
        final String initResponseBody = "{\n"
            + "\"response\": {\n"
            + " \"code\": \"SUCCESS\",\n"
            + " \"data\": {\n"
            + "  \"glossaryImport\": {\n"
            + "    \"glossaryUid\": \"bad90990-4be1-4570-81da-b719cddd5352\",\n"
            + "    \"importUid\": \"939a4439-2ed0-4123-bc68-d188e4d664ab\",\n"
            + "     \"importStatus\": \"PENDING\"\n"
            + "   },\n"
            + "   \"entryChanges\": {\n"
            + "     \"newEntries\": 0,\n"
            + "     \"existingEntryUpdates\": 1,\n"
            + "     \"notMatchedEntries\": 1,\n"
            + "     \"entriesToArchive\": 0\n"
            + "   },\n"
            + "   \"translationChanges\": [{\n"
            + "    \"localeId\": \"en-US\",\n"
            + "    \"newTranslations\": 0,\n"
            + "    \"updatedTranslations\": 1,\n"
            + "    \"translationsToRemove\": 0\n"
            + "   }, {\n"
            + "    \"localeId\": \"uk-UA\",\n"
            + "    \"newTranslations\": 0,\n"
            + "    \"updatedTranslations\": 1,\n"
            + "    \"translationsToRemove\": 0\n"
            + "   }],\n"
            + "   \"warnings\": []\n"
            + "  }\n"
            + " }\n"
            + "}";

        assignResponse(200, initResponseBody);

        final GlossaryImportResponsePTO importResponse = glossaryApi.initializeGlossaryImport("account", "bad90990-4be1-4570-81da-b719cddd5352",
            GlossaryImportCommandPTO.builder().
                archiveMode(false).
                importFileMediaType(FILE_MEDIA_TYPE).
                importFileName(FILE_URI).
                importFile(new ByteArrayInputStream("\"ID\",\"Description\"".getBytes())).
                build());

        assertNotNull(importResponse.getEntryChanges());
        assertNotNull(importResponse.getGlossaryImport());
        assertNotNull(importResponse.getTranslationChanges());
        assertNotNull(importResponse.getWarnings());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());

    }

    @Test
    public void testImportStatus() throws InterruptedException {

        final String initResponseBody = "{\n"
            + " \"response\": {\n"
            + "  \"code\": \"SUCCESS\",\n"
            + "  \"data\": {\n"
            + "   \"glossaryUid\": \"bad90990-4be1-4570-81da-b719cddd5352\",\n"
            + "   \"importUid\": \"939a4439-2ed0-4123-bc68-d188e4d664ab\",\n"
            + "   \"importStatus\": \"PENDING\"\n"
            + "  }\n"
            + " }\n"
            + "}";

        assignResponse(200, initResponseBody);

        final GlossaryImportPTO importStatus = glossaryApi.importStatus("account", "bad90990-4be1-4570-81da-b719cddd5352",
            "939a4439-2ed0-4123-bc68-d188e4d664ab");

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("PENDING", importStatus.getImportStatus());

    }

    @Test
    public void testConfirmImport() throws InterruptedException {

        final String initResponseBody = "{\n"
            + " \"response\": {\n"
            + "  \"code\": \"SUCCESS\",\n"
            + "  \"data\": {\n"
            + "   \"glossaryUid\": \"bad90990-4be1-4570-81da-b719cddd5352\",\n"
            + "   \"importUid\": \"939a4439-2ed0-4123-bc68-d188e4d664ab\",\n"
            + "   \"importStatus\": \"IN_PROGRESS\"\n"
            + "  }\n"
            + " }\n"
            + "}";

        assignResponse(200, initResponseBody);

        final GlossaryImportPTO importStatus = glossaryApi.confirmGlossaryImport("account", "bad90990-4be1-4570-81da-b719cddd5352",
            "939a4439-2ed0-4123-bc68-d188e4d664ab");

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("IN_PROGRESS", importStatus.getImportStatus());

    }

    @Test
    public void testExport() throws IOException, InterruptedException {
        final String body = "\"ID\",\"Definition\"";
        final MockResponse response = new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_LENGTH, body.length())
            .setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
            .setBody(body);

        mockWebServer.enqueue(response);
        final GlossaryEntriesExportCommandPTO command = GlossaryEntriesExportCommandPTO.builder().
            localeIds(Arrays.asList("uk-UA", "en-US")).
            focusLocaleId("uk-UA").
            format("CSV").
            filter(GetGlossaryEntriesByFilterCommandPTO.builder().build()).
            build();

        final InputStream exportResult = glossaryApi.exportGlossary("account", "bad90990-4be1-4570-81da-b719cddd5352", command);

        Assert.assertNotNull(exportResult);
        final String data = IOUtils.toString(exportResult, StandardCharsets.UTF_8);
        exportResult.close();
        Assert.assertEquals(body, data);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());

    }

    @Test
    public void testReadLabels() throws InterruptedException {

        final String allLabels = "{\n"
            + "\t\"response\": {\n"
            + "\t\t\"code\": \"SUCCESS\",\n"
            + "\t\t\"data\": {\n"
            + "\t\t\t\"totalCount\": 5,\n"
            + "\t\t\t\"items\": [{\n"
            + "\t\t\t\t\"labelUid\": \"16ed66cc-accc-4bb5-9822-bc84e93429f8\",\n"
            + "\t\t\t\t\"labelText\": \"label-2\"\n"
            + "\t\t\t}, {\n"
            + "\t\t\t\t\"labelUid\": \"69dae398-96c2-45f6-9f0d-91470c3464bd\",\n"
            + "\t\t\t\t\"labelText\": \"label-3\"\n"
            + "\t\t\t}, {\n"
            + "\t\t\t\t\"labelUid\": \"12bbcf34-bd13-4d3b-8ab9-fd5dd8e93eb8\",\n"
            + "\t\t\t\t\"labelText\": \"label-4\"\n"
            + "\t\t\t}, {\n"
            + "\t\t\t\t\"labelUid\": \"acd257ea-cf47-436b-9b8c-60e0994f173d\",\n"
            + "\t\t\t\t\"labelText\": \"label-R-Test\"\n"
            + "\t\t\t}, {\n"
            + "\t\t\t\t\"labelUid\": \"8c2aba44-9058-4140-abe2-e06bb18da900\",\n"
            + "\t\t\t\t\"labelText\": \"was l 1- become  even other\"\n"
            + "\t\t\t}]\n"
            + "\t\t}\n"
            + "\t}\n"
            + "}";

        assignResponse(200, allLabels);

        final ListResponse<GlossaryLabelPTO> readResult = glossaryApi.readAllGlossaryLabels("account");

        Assert.assertNotNull(readResult);
        Assert.assertEquals(readResult.getTotalCount(), 5);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());

    }

    @Test
    public void testAuthForTranslation() throws InterruptedException {
        final String asyncResponse =
            "{\"response\":{\"code\":\"ACCEPTED\",\"data\":{\"operationUid\":\"e2debbe4-8be0-4552-b7b0-364c04a8a3c3\"}}}";

        assignResponse(202, asyncResponse);

        final AsyncOperationResponsePTO authOperationUid = glossaryApi.authorizeEntriesForTranslation("account",
            "e2debbe4-8be0-4552-b7b0-364c04a8a3c6",
            EntriesAuthorizationCommandPTO.builder().
                sourceLocale("en").
                projectId("my-Project").
                filter(GetGlossaryEntriesByFilterCommandPTO.builder().entryState("BOTH").build()).
                localeWorkflow(LocaleWorkflowPairCommandPTO.builder().
                    localeId("uk-UA").
                    workflowUid("wf-1").
                    build()).
                build()
        );

        Assert.assertNotNull(authOperationUid);
        Assert.assertEquals(authOperationUid.getOperationUid(), "e2debbe4-8be0-4552-b7b0-364c04a8a3c3");

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());

    }

    @Test
    public void testCreateLabel() throws InterruptedException {
        final String create =
            "{\"response\":{\"code\":\"SUCCESS\",\"data\":{\"labelUid\":\"e2debbe4-8be0-4552-b7b0-364c04a8a3c3\",\"labelText\":\"my-test\"}}}";

        assignResponse(200, create);

        final GlossaryLabelPTO createResult = glossaryApi.createGlossaryLabel("account", GlossaryLabelCommandPTO.builder().labelText("my-test").build());

        Assert.assertNotNull(createResult);
        Assert.assertEquals(createResult.getLabelText(), "my-test");

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());

    }

    @Test
    public void testUpdate() throws InterruptedException {
        final String update =
            "{\"response\":{\"code\":\"SUCCESS\",\"data\":{\"labelUid\":\"e2debbe4-8be0-4552-b7b0-364c04a8a3c3\",\"labelText\":\"my-test\"}}}";

        assignResponse(200, update);

        final GlossaryLabelPTO updateLabel = glossaryApi
            .updateGlossaryLabel("account", "e2debbe4-8be0-4552-b7b0-364c04a8a3c3", GlossaryLabelCommandPTO.builder().labelText("my-test").build());

        Assert.assertNotNull(updateLabel);
        Assert.assertEquals(updateLabel.getLabelText(), "my-test");

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("PUT", recordedRequest.getMethod());

    }

    @Test
    public void testDelete() throws InterruptedException {
        final String delete = "{\"response\":{\"code\":\"SUCCESS\",\"data\":{}}}";

        assignResponse(200, delete);

        final EmptyData remove = glossaryApi.deleteGlossaryLabel("account", "e2debbe4-8be0-4552-b7b0-364c04a8a3c3");

        Assert.assertNotNull(remove);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("DELETE", recordedRequest.getMethod());

    }

}
