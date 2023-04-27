package com.smartling.api.accounts.v2;

import com.smartling.api.accounts.v2.pto.GetProjectsListPTO;
import com.smartling.api.accounts.v2.pto.ProjectDetailsPTO;
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
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AccountsApiTest {
    private final static String TOKEN = "token";
    private final static String ACCOUNT_UID = "2f23eaae9";

    private MockWebServer mockWebServer;
    private AccountsApi accountsApi;

    @Before
    public void setUp() throws Exception
    {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final AccountsApiFactory factory = new AccountsApiFactory();
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter(TOKEN);
        final ClientConfiguration config = DefaultClientConfiguration.builder()
            .baseUrl(mockWebServer.url("/").url())
            .build();

        accountsApi = factory.buildApi(tokenFilter, config);
    }

    @After
    public void tearDown() throws Exception
    {
        mockWebServer.shutdown();
    }

    @Test
    public void getProjectsByAccount() throws InterruptedException, IOException
    {
        mockResponse(HttpStatus.SC_OK, "/projectsByAccountResponse.json");

        final ListResponse<ProjectDetailsPTO> projects = accountsApi.getProjectsByAccount(
            ACCOUNT_UID,
            GetProjectsListPTO.builder()
                .projectNameFilter("name")
                .includeArchived(true)
                .projectTypeCode("GDN")
                .offset(10)
                .limit(5)
                .build()
        );

        final RecordedRequest request = mockWebServer.takeRequest();

        String requestPath = request.getPath();

        assertThat(request.getMethod(), is("GET"));
        assertThat(requestPath, containsString("/accounts-api/v2/accounts/" + ACCOUNT_UID + "/projects"));
        assertThat(requestPath, containsString("projectNameFilter=name"));
        assertThat(requestPath, containsString("includeArchived=true"));
        assertThat(requestPath, containsString("offset=10"));
        assertThat(requestPath, containsString("limit=5"));
        assertThat(requestPath, containsString("projectTypeCode=GDN"));

        assertThat(projects.getTotalCount(), is(2L));
        assertThat(projects.getItems().size(), is(2));
        assertThat(projects.getItems().get(0), is(ProjectDetailsPTO.builder()
            .accountUid(ACCOUNT_UID)
            .archived(true)
            .packageUid("8dab856631c8")
            .projectId("2ab430ac1")
            .projectName("Crawler Creator Test")
            .projectTypeCode("GDN")
            .sourceLocaleDescription("English (United States) [en-US]")
            .sourceLocaleId("en-US")
            .build()));
        assertThat(projects.getItems().get(1), is(ProjectDetailsPTO.builder()
            .accountUid(ACCOUNT_UID)
            .archived(false)
            .packageUid("8dab856631c8")
            .projectId("f58140a34")
            .projectName("GDN Protected Test")
            .projectTypeCode("GDN")
            .sourceLocaleDescription("English (United States) [en-US]")
            .sourceLocaleId("en-US")
            .build()));
    }

    @SuppressWarnings("SameParameterValue")
    private void mockResponse(final int httpStatusCode, final String responseResourceName) throws IOException
    {
        String responseBody = IOUtils.resourceToString(responseResourceName, StandardCharsets.UTF_8);

        final MockResponse response = new MockResponse()
            .setResponseCode(httpStatusCode)
            .setHeader(HttpHeaders.CONTENT_LENGTH, responseBody.length())
            .setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
            .setBody(responseBody);

        mockWebServer.enqueue(response);
    }
}
