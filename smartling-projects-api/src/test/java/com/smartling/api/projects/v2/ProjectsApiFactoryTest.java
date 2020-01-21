package com.smartling.api.projects.v2;

import com.smartling.api.projects.v2.pto.LocaleDetailsPTO;
import com.smartling.api.projects.v2.pto.ProjectDetailsPTO;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
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

public class ProjectsApiFactoryTest
{
    private static final String SUCCESS_RESPONSE_ENVELOPE = "{ \"response\": { \"code\": \"SUCCESS\", \"data\": %s } })";

    private static final String GET_PROJECTS_DETAILS_DATA_JSON =
        "{\n" +
            "      \"accountUid\": \"at724e365\",\n" +
            "      \"archived\": true,\n" +
            "      \"packageUid\": \"ed7544765\",\n" +
            "      \"projectId\": \"fd7244365\",\n" +
            "      \"projectName\": \"Android XML Content\",\n" +
            "      \"projectTypeCode\": \"APPLICATION_RESOURCES\",\n" +
            "      \"projectTypeDisplayValue\": \"Application resources\",\n" +
            "      \"sourceLocaleDescription\": \"English\",\n" +
            "      \"sourceLocaleId\": \"en-US\",\n" +
            "      \"targetLocales\": [\n" +
            "        {\n" +
            "          \"description\": \"German (Germany)\",\n" +
            "          \"enabled\": true,\n" +
            "          \"localeId\": \"de-DE\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"description\": \"French (France)\",\n" +
            "          \"enabled\": false,\n" +
            "          \"localeId\": \"fr-FR\"\n" +
            "        }\n" +
            "      ]\n" +
            "}";

    private MockWebServer mockWebServer;
    private ProjectsApi projectsApi;

    @Before
    public void setUp() throws Exception
    {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final ProjectsApiFactory factory = new ProjectsApiFactory();
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("foo");
        final ClientConfiguration config = DefaultClientConfiguration.builder().baseUrl(mockWebServer.url("/").url()).build();

        projectsApi = factory.buildApi(tokenFilter, config);
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
    public void testProjectDetails() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, GET_PROJECTS_DETAILS_DATA_JSON));

        final ProjectDetailsPTO projectDetails = projectsApi.getDetails("fd7244365", true);

        final RecordedRequest request = mockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertTrue(request.getPath().contains("/projects-api/v2/projects/fd7244365"));
        assertTrue(request.getPath().contains("includeDisabledLocales=true"));

        assertNotNull(projectDetails);
        assertEquals(projectDetails.getAccountUid(), "at724e365");
        assertEquals(projectDetails.getProjectId(), "fd7244365");
        assertTrue(projectDetails.getArchived());
        assertEquals(projectDetails.getPackageUid(), "ed7544765");
        assertEquals(projectDetails.getProjectName(), "Android XML Content");
        assertEquals(projectDetails.getProjectTypeCode(), "APPLICATION_RESOURCES");
        assertEquals(projectDetails.getProjectTypeDisplayValue(), "Application resources");
        assertEquals(projectDetails.getSourceLocaleDescription(), "English");
        assertEquals(projectDetails.getSourceLocaleId(), "en-US");
        assertEquals(projectDetails.getTargetLocales().size(), 2);
        validateLocaleDetailsPTO(projectDetails.getTargetLocales().get(0), "German (Germany)", true, "de-DE");
        validateLocaleDetailsPTO(projectDetails.getTargetLocales().get(1), "French (France)", false, "fr-FR");
    }

    private void validateLocaleDetailsPTO(LocaleDetailsPTO localeDetailsPTO, String description, boolean enabled, String localeId)
    {
        assertEquals(localeDetailsPTO.getDescription(), description);
        assertEquals(localeDetailsPTO.isEnabled(), enabled);
        assertEquals(localeDetailsPTO.getLocaleId(), localeId);
    }
}
