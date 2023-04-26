package com.smartling.glossary.v3.components;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.v2.response.ListResponse;
import com.smartling.glossary.v3.pto.GlossariesArchiveUnarchivedResponsePTO;
import com.smartling.glossary.v3.pto.GlossaryEntriesCountsResponsePTO;
import com.smartling.glossary.v3.pto.GlossaryResponsePTO;
import com.smartling.glossary.v3.pto.GlossarySearchResponsePTO;
import com.smartling.glossary.v3.pto.command.GetGlossariesByFilterCommandPTO;
import com.smartling.glossary.v3.pto.command.GetGlossariesWithEntriesCountByFilterCommandPTO;
import com.smartling.glossary.v3.pto.command.GlossariesArchiveUnarchiveCommandPTO;
import com.smartling.glossary.v3.pto.command.GlossaryCommandPTO;
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

public class GlossaryManagementApiTest {

    private MockWebServer mockWebServer;

    private GlossaryManagementApi glossaryManagementApi;

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

        final GlossaryManagementApiFactory factory = new GlossaryManagementApiFactory();
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("foo");
        final ClientConfiguration config = DefaultClientConfiguration.builder().baseUrl(mockWebServer.url("/").url()).build();

        glossaryManagementApi = factory.buildApi(tokenFilter, config);
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

        GlossaryResponsePTO glossaryResponsePTO = glossaryManagementApi.readGlossary("56a0b220", "bad90990-4be1-4570-81da-b719cddd5352");

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

        GlossaryResponsePTO glossaryResponsePTO = glossaryManagementApi
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

        GlossaryResponsePTO glossaryResponsePTO = glossaryManagementApi
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

        GlossariesArchiveUnarchivedResponsePTO glossaryResponsePTO = glossaryManagementApi
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

        GlossariesArchiveUnarchivedResponsePTO glossaryResponsePTO = glossaryManagementApi
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

        ListResponse<GlossaryEntriesCountsResponsePTO> glossaryResponsePTO = glossaryManagementApi.searchGlossariesWithEntriesCounts("56a0b220",
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

        ListResponse<GlossarySearchResponsePTO> glossaryResponsePTO = glossaryManagementApi.searchGlossaries("56a0b220",
            GetGlossariesWithEntriesCountByFilterCommandPTO.builder().query("fall").glossaryState("BOTH").build()
        );

        assertNotNull(glossaryResponsePTO);
        assertEquals(glossaryResponsePTO.getTotalCount(), 1);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());

    }
}
