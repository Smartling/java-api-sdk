package com.smartling.api.glossary.v3.components.ie;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.api.glossary.v3.pto.entry.command.filter.GetGlossaryEntriesByFilterCommandPTO;
import com.smartling.api.glossary.v3.pto.ie.GlossaryImportPTO;
import com.smartling.api.glossary.v3.pto.ie.GlossaryImportResponsePTO;
import com.smartling.api.glossary.v3.pto.ie.command.GlossaryEntriesExportCommandPTO;
import com.smartling.api.glossary.v3.pto.ie.command.GlossaryImportCommandPTO;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ImportExportApiTest {

    private final static String FILE_URI = "glossary.csv";
    private final static String FILE_MEDIA_TYPE = "text/csv";

    private MockWebServer mockWebServer;

    private ImportExportApi ieApi;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final ImportExportApiFactory factory = new ImportExportApiFactory();
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("foo");
        final ClientConfiguration config = DefaultClientConfiguration.builder().baseUrl(mockWebServer.url("/").url()).build();

        ieApi = factory.buildApi(tokenFilter, config);
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

        final GlossaryImportResponsePTO importResponse = ieApi.initializeGlossaryImport("account", "bad90990-4be1-4570-81da-b719cddd5352",
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

        final GlossaryImportPTO importStatus = ieApi.importStatus("account", "bad90990-4be1-4570-81da-b719cddd5352",
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

        final GlossaryImportPTO importStatus = ieApi.confirmGlossaryImport("account", "bad90990-4be1-4570-81da-b719cddd5352",
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

        final InputStream exportResult = ieApi.exportGlossary("account", "bad90990-4be1-4570-81da-b719cddd5352", command);

        Assert.assertNotNull(exportResult);
        final String data = IOUtils.toString(exportResult, StandardCharsets.UTF_8);
        exportResult.close();
        Assert.assertEquals(body, data);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());

    }

}
