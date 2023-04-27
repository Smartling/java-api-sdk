package com.smartling.api.glossary.v3.components.entry;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.api.glossary.v3.pto.AsyncOperationResponsePTO;
import com.smartling.api.glossary.v3.pto.entry.GlossaryEntryDetailedResponsePTO;
import com.smartling.api.glossary.v3.pto.entry.command.EntriesBulkActionCommandPTO;
import com.smartling.api.glossary.v3.pto.entry.command.EntriesBulkUpdateLabelsCommandPTO;
import com.smartling.api.glossary.v3.pto.entry.command.GlossaryEntryCommandPTO;
import com.smartling.api.glossary.v3.pto.entry.command.filter.GetGlossaryEntriesByFilterCommandPTO;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.v2.response.ListResponse;
import com.smartling.api.glossary.v3.pto.entry.GlossaryEntryResponsePTO;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EntryManagementApiTest {

    private MockWebServer mockWebServer;

    private EntryManagementApi entriesAPi;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private static final String ENTRY_RESPONSE = "{\n"
        + "    \"response\": {\n"
        + "        \"code\": \"SUCCESS\",\n"
        + "        \"data\": {\n"
        + "            \"entryUid\": \"37915662-1f42-4a51-afe4-a9eaf32dfa37\",\n"
        + "            \"glossaryUid\": \"bad90990-4be1-4570-81da-b719cddd5352\",\n"
        + "            \"definition\": \"no EN  present in DB\",\n"
        + "            \"partOfSpeech\": null,\n"
        + "            \"labelUids\": [],\n"
        + "            \"translations\": [\n"
        + "                {\n"
        + "                    \"localeId\": \"en-US\",\n"
        + "                    \"fallbackLocaleId\": null,\n"
        + "                    \"term\": \"4\",\n"
        + "                    \"notes\": null,\n"
        + "                    \"caseSensitive\": false,\n"
        + "                    \"exactMatch\": false,\n"
        + "                    \"doNotTranslate\": false,\n"
        + "                    \"disabled\": false,\n"
        + "                    \"variants\": [],\n"
        + "                    \"customFieldValues\": [],\n"
        + "                    \"createdByUserUid\": \"671b520051b2\",\n"
        + "                    \"modifiedByUserUid\": \"671b520051b2\",\n"
        + "                    \"createdDate\": \"2023-03-30T10:38:09Z\",\n"
        + "                    \"modifiedDate\": \"2023-03-30T10:38:09Z\"\n"
        + "                },\n"
        + "                {\n"
        + "                    \"localeId\": \"uk-UA\",\n"
        + "                    \"fallbackLocaleId\": null,\n"
        + "                    \"term\": \"4\",\n"
        + "                    \"notes\": null,\n"
        + "                    \"caseSensitive\": false,\n"
        + "                    \"exactMatch\": false,\n"
        + "                    \"doNotTranslate\": false,\n"
        + "                    \"disabled\": false,\n"
        + "                    \"variants\": [],\n"
        + "                    \"customFieldValues\": [],\n"
        + "                    \"createdByUserUid\": \"671b520051b2\",\n"
        + "                    \"modifiedByUserUid\": \"671b520051b2\",\n"
        + "                    \"createdDate\": \"2023-03-30T10:38:43Z\",\n"
        + "                    \"modifiedDate\": \"2023-03-30T10:38:43Z\"\n"
        + "                }\n"
        + "            ],\n"
        + "            \"customFieldValues\": [],\n"
        + "            \"archived\": false,\n"
        + "            \"createdByUserUid\": \"671b520051b2\",\n"
        + "            \"modifiedByUserUid\": \"671b520051b2\",\n"
        + "            \"createdDate\": \"2023-03-30T10:38:09Z\",\n"
        + "            \"modifiedDate\": \"2023-03-30T10:38:43Z\"\n"
        + "        }\n"
        + "    }\n"
        + "}";

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final EntryManagementApiFactory factory = new EntryManagementApiFactory();
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("foo");
        final ClientConfiguration config = DefaultClientConfiguration.builder().baseUrl(mockWebServer.url("/").url()).build();

        entriesAPi = factory.buildApi(tokenFilter, config);
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
    public void testReadGlossaryEntry() throws Exception {

        assignResponse(200, ENTRY_RESPONSE);

        GlossaryEntryDetailedResponsePTO glossaryResponsePTO = entriesAPi.readGlossaryEntry("56a0b220", "bad90990-4be1-4570-81da-b719cddd5352",
            "37915662-1f42-4a51-afe4-a9eaf32dfa37");

        assertEquals("bad90990-4be1-4570-81da-b719cddd5352", glossaryResponsePTO.getGlossaryUid());
        assertEquals("37915662-1f42-4a51-afe4-a9eaf32dfa37", glossaryResponsePTO.getEntryUid());
        assertEquals("no EN  present in DB", glossaryResponsePTO.getDefinition());
        assertNotNull(glossaryResponsePTO.getTranslations());
        assertNotNull(glossaryResponsePTO.getLabelUids());
        assertNotNull(glossaryResponsePTO.getCustomFieldValues());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());

    }

    @Test
    public void testCreateGlossaryEntry() throws Exception {

        assignResponse(200, ENTRY_RESPONSE);

        GlossaryEntryDetailedResponsePTO glossaryResponsePTO = entriesAPi.createGlossaryEntry("56a0b220", "bad90990-4be1-4570-81da-b719cddd5352",
            GlossaryEntryCommandPTO.builder().build());

        assertEquals("bad90990-4be1-4570-81da-b719cddd5352", glossaryResponsePTO.getGlossaryUid());
        assertEquals("37915662-1f42-4a51-afe4-a9eaf32dfa37", glossaryResponsePTO.getEntryUid());
        assertEquals("no EN  present in DB", glossaryResponsePTO.getDefinition());
        assertNotNull(glossaryResponsePTO.getTranslations());
        assertNotNull(glossaryResponsePTO.getLabelUids());
        assertNotNull(glossaryResponsePTO.getCustomFieldValues());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());

    }

    @Test
    public void testUpdateGlossaryEntry() throws Exception {

        assignResponse(200, ENTRY_RESPONSE);

        GlossaryEntryDetailedResponsePTO glossaryResponsePTO = entriesAPi.updateGlossaryEntry("56a0b220", "bad90990-4be1-4570-81da-b719cddd5352",
            "37915662-1f42-4a51-afe4-a9eaf32dfa37", GlossaryEntryCommandPTO.builder().build());

        assertEquals("bad90990-4be1-4570-81da-b719cddd5352", glossaryResponsePTO.getGlossaryUid());
        assertEquals("37915662-1f42-4a51-afe4-a9eaf32dfa37", glossaryResponsePTO.getEntryUid());
        assertEquals("no EN  present in DB", glossaryResponsePTO.getDefinition());
        assertNotNull(glossaryResponsePTO.getTranslations());
        assertNotNull(glossaryResponsePTO.getLabelUids());
        assertNotNull(glossaryResponsePTO.getCustomFieldValues());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("PUT", recordedRequest.getMethod());

    }

    @Test
    public void testEntriesSearch() throws Exception {
        final String searchResponse = "{\n"
            + "\t\"response\": {\n"
            + "\t\t\"code\": \"SUCCESS\",\n"
            + "\t\t\"data\": {\n"
            + "\t\t\t\"totalCount\": 2,\n"
            + "\t\t\t\"items\": [{\n"
            + "\t\t\t\t\"entryUid\": \"37915662-1f42-4a51-afe4-a9eaf32dfa37\",\n"
            + "\t\t\t\t\"glossaryUid\": \"bad90990-4be1-4570-81da-b719cddd5352\",\n"
            + "\t\t\t\t\"definition\": \"no EN  present in DB\",\n"
            + "\t\t\t\t\"partOfSpeech\": null,\n"
            + "\t\t\t\t\"labelUids\": [],\n"
            + "\t\t\t\t\"translations\": [{\n"
            + "\t\t\t\t\t\"localeId\": \"en-US\",\n"
            + "\t\t\t\t\t\"fallbackLocaleId\": null,\n"
            + "\t\t\t\t\t\"term\": \"4\",\n"
            + "\t\t\t\t\t\"notes\": null,\n"
            + "\t\t\t\t\t\"caseSensitive\": false,\n"
            + "\t\t\t\t\t\"exactMatch\": false,\n"
            + "\t\t\t\t\t\"doNotTranslate\": false,\n"
            + "\t\t\t\t\t\"disabled\": false,\n"
            + "\t\t\t\t\t\"variants\": [],\n"
            + "\t\t\t\t\t\"customFieldValues\": [],\n"
            + "\t\t\t\t\t\"createdByUserUid\": \"671b520051b2\",\n"
            + "\t\t\t\t\t\"modifiedByUserUid\": \"671b520051b2\",\n"
            + "\t\t\t\t\t\"createdDate\": \"2023-03-30T10:38:09Z\",\n"
            + "\t\t\t\t\t\"modifiedDate\": \"2023-03-30T10:38:09Z\"\n"
            + "\t\t\t\t}],\n"
            + "\t\t\t\t\"customFieldValues\": [],\n"
            + "\t\t\t\t\"archived\": false,\n"
            + "\t\t\t\t\"createdByUserUid\": \"671b520051b2\",\n"
            + "\t\t\t\t\"modifiedByUserUid\": \"671b520051b2\",\n"
            + "\t\t\t\t\"createdDate\": \"2023-03-30T10:38:09Z\",\n"
            + "\t\t\t\t\"modifiedDate\": \"2023-03-30T10:38:43Z\"\n"
            + "\t\t\t}, {\n"
            + "\t\t\t\t\"entryUid\": \"d71c254c-3aba-43db-b93d-dd6d8ca8f334\",\n"
            + "\t\t\t\t\"glossaryUid\": \"bad90990-4be1-4570-81da-b719cddd5352\",\n"
            + "\t\t\t\t\"definition\": \"en present in DB\",\n"
            + "\t\t\t\t\"partOfSpeech\": null,\n"
            + "\t\t\t\t\"labelUids\": [],\n"
            + "\t\t\t\t\"translations\": [{\n"
            + "\t\t\t\t\t\"localeId\": \"en-US\",\n"
            + "\t\t\t\t\t\"fallbackLocaleId\": null,\n"
            + "\t\t\t\t\t\"term\": \"4\",\n"
            + "\t\t\t\t\t\"notes\": null,\n"
            + "\t\t\t\t\t\"caseSensitive\": false,\n"
            + "\t\t\t\t\t\"exactMatch\": false,\n"
            + "\t\t\t\t\t\"doNotTranslate\": false,\n"
            + "\t\t\t\t\t\"disabled\": false,\n"
            + "\t\t\t\t\t\"variants\": [],\n"
            + "\t\t\t\t\t\"customFieldValues\": [],\n"
            + "\t\t\t\t\t\"createdByUserUid\": \"671b520051b2\",\n"
            + "\t\t\t\t\t\"modifiedByUserUid\": \"671b520051b2\",\n"
            + "\t\t\t\t\t\"createdDate\": \"2023-03-30T10:38:25Z\",\n"
            + "\t\t\t\t\t\"modifiedDate\": \"2023-03-30T10:38:25Z\"\n"
            + "\t\t\t\t}],\n"
            + "\t\t\t\t\"customFieldValues\": [],\n"
            + "\t\t\t\t\"archived\": false,\n"
            + "\t\t\t\t\"createdByUserUid\": \"671b520051b2\",\n"
            + "\t\t\t\t\"modifiedByUserUid\": \"671b520051b2\",\n"
            + "\t\t\t\t\"createdDate\": \"2023-03-30T10:38:25Z\",\n"
            + "\t\t\t\t\"modifiedDate\": \"2023-03-30T10:38:38Z\"\n"
            + "\t\t\t}]\n"
            + "\t\t}\n"
            + "\t}\n"
            + "}";
        assignResponse(200, searchResponse);

        ListResponse<GlossaryEntryResponsePTO> glossaryResponsePTO = entriesAPi.searchGlossaryEntries("56a0b220", "bad90990-4be1-4570-81da-b719cddd5352",
            GetGlossaryEntriesByFilterCommandPTO.builder().query("4").entryState("BOTH").build());

        assertNotNull(glossaryResponsePTO);
        assertNotNull(glossaryResponsePTO.getItems());
        assertEquals(2, glossaryResponsePTO.getTotalCount());
        assertEquals(2, glossaryResponsePTO.getItems().size());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());

    }


    @Test
    public void testBulkArchive() throws Exception {
        final String asyncResponse = "{\"response\":{\"code\":\"ACCEPTED\",\"data\":{\"operationUid\":\"2982d939-8d5b-47f4-931b-15622d38997a\"}}}";
        assignResponse(200, asyncResponse);

        AsyncOperationResponsePTO glossaryResponsePTO = entriesAPi.archiveGlossaryEntries("56a0b220",
            "bad90990-4be1-4570-81da-b719cddd5352",
            EntriesBulkActionCommandPTO.builder().filter(GetGlossaryEntriesByFilterCommandPTO.builder().build()).build());

        assertNotNull(glossaryResponsePTO);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());

    }

    @Test
    public void testBulkRestore() throws Exception {
        final String asyncResponse = "{\"response\":{\"code\":\"ACCEPTED\",\"data\":{\"operationUid\":\"2982d939-8d5b-47f4-931b-15622d38997a\"}}}";
        assignResponse(200, asyncResponse);

        AsyncOperationResponsePTO glossaryResponsePTO = entriesAPi.restoreGlossaryEntries("56a0b220",
            "bad90990-4be1-4570-81da-b719cddd5352",
            EntriesBulkActionCommandPTO.builder().filter(GetGlossaryEntriesByFilterCommandPTO.builder().build()).build());

        assertNotNull(glossaryResponsePTO);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());

    }

    @Test
    public void testAddLabels() throws Exception {
        final String asyncResponse = "{\"response\":{\"code\":\"ACCEPTED\",\"data\":{\"operationUid\":\"2982d939-8d5b-47f4-931b-15622d38997a\"}}}";
        assignResponse(200, asyncResponse);

        AsyncOperationResponsePTO glossaryResponsePTO = entriesAPi.addLabelsToGlossaryEntries("56a0b220",
            "bad90990-4be1-4570-81da-b719cddd5352",
            EntriesBulkUpdateLabelsCommandPTO.builder().labelUids(Collections.singleton("lab-1")).
                filter(GetGlossaryEntriesByFilterCommandPTO.builder().build()).
                build());

        assertNotNull(glossaryResponsePTO);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
    }

    @Test
    public void testRemoveLabels() throws Exception {
        final String asyncResponse = "{\"response\":{\"code\":\"ACCEPTED\",\"data\":{\"operationUid\":\"2982d939-8d5b-47f4-931b-15622d38997a\"}}}";
        assignResponse(200, asyncResponse);

        AsyncOperationResponsePTO glossaryResponsePTO = entriesAPi.removeLabelsFromGlossaryEntries("56a0b220",
            "bad90990-4be1-4570-81da-b719cddd5352", EntriesBulkUpdateLabelsCommandPTO.builder().
                labelUids(Collections.singleton("lab-1")).
                filter(GetGlossaryEntriesByFilterCommandPTO.builder().build()).
                build());

        assertNotNull(glossaryResponsePTO);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
    }

    @Test
    public void testDeleteEntries() throws Exception {
        final String asyncResponse = "{\"response\":{\"code\":\"ACCEPTED\",\"data\":{\"operationUid\":\"2982d939-8d5b-47f4-931b-15622d38997a\"}}}";
        assignResponse(200, asyncResponse);

        AsyncOperationResponsePTO glossaryResponsePTO = entriesAPi.removeGlossaryEntries("56a0b220",
            "bad90990-4be1-4570-81da-b719cddd5352", EntriesBulkActionCommandPTO.builder().
                filter(GetGlossaryEntriesByFilterCommandPTO.builder().build()).
                build());

        assertNotNull(glossaryResponsePTO);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
    }
}
