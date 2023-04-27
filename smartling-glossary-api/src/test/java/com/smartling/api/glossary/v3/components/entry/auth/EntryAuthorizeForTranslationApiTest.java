package com.smartling.api.glossary.v3.components.entry.auth;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.glossary.v3.pto.AsyncOperationResponsePTO;
import com.smartling.api.glossary.v3.pto.authorize.EntriesAuthorizationCommandPTO;
import com.smartling.api.glossary.v3.pto.authorize.LocaleWorkflowPairCommandPTO;
import com.smartling.api.glossary.v3.pto.entry.command.filter.GetGlossaryEntriesByFilterCommandPTO;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;

import static org.junit.Assert.*;


public class EntryAuthorizeForTranslationApiTest {


    private MockWebServer mockWebServer;

    private EntryAuthorizeForTranslationApi entrisAuthApi;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final EntryAuthorizeForTranslationApiFactory factory = new EntryAuthorizeForTranslationApiFactory();
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("foo");
        final ClientConfiguration config = DefaultClientConfiguration.builder().baseUrl(mockWebServer.url("/").url()).build();

        entrisAuthApi = factory.buildApi(tokenFilter, config);
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
    public void testAuthForTranslation() throws InterruptedException {
        final String asyncResponse =
            "{\"response\":{\"code\":\"ACCEPTED\",\"data\":{\"operationUid\":\"e2debbe4-8be0-4552-b7b0-364c04a8a3c3\"}}}";

        assignResponse(202, asyncResponse);

        final AsyncOperationResponsePTO authOperationUid = entrisAuthApi.authorizeEntriesForTranslation("account",
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

}
