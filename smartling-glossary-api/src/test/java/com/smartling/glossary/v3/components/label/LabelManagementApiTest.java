package com.smartling.glossary.v3.components.label;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.v2.response.EmptyData;
import com.smartling.api.v2.response.ListResponse;
import com.smartling.glossary.v3.pto.label.GlossaryLabelCommandPTO;
import com.smartling.glossary.v3.pto.label.GlossaryLabelPTO;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;

import static org.junit.Assert.assertEquals;

public class LabelManagementApiTest {

    private MockWebServer mockWebServer;

    private LabelManagementApi labelManagementApi;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final LabelManagementApiFactory factory = new LabelManagementApiFactory();
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("foo");
        final ClientConfiguration config = DefaultClientConfiguration.builder().baseUrl(mockWebServer.url("/").url()).build();

        labelManagementApi = factory.buildApi(tokenFilter, config);
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

        final ListResponse<GlossaryLabelPTO> readResult = labelManagementApi.readAllGlossaryLabels("account");

        Assert.assertNotNull(readResult);
        Assert.assertEquals(readResult.getTotalCount(), 5);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());

    }


    @Test
    public void testCreateLabel() throws InterruptedException {
        final String create =
            "{\"response\":{\"code\":\"SUCCESS\",\"data\":{\"labelUid\":\"e2debbe4-8be0-4552-b7b0-364c04a8a3c3\",\"labelText\":\"my-test\"}}}";

        assignResponse(200, create);

        final GlossaryLabelPTO createResult = labelManagementApi.createGlossaryLabel("account", GlossaryLabelCommandPTO.builder().labelText("my-test").build());

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

        final GlossaryLabelPTO updateLabel = labelManagementApi
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

        final EmptyData remove = labelManagementApi.deleteGlossaryLabel("account", "e2debbe4-8be0-4552-b7b0-364c04a8a3c3");

        Assert.assertNotNull(remove);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("DELETE", recordedRequest.getMethod());

    }
}
