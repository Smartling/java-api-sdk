package com.smartling.sdk.authorization;

import com.smartling.api.external.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.sdk.authorization.pto.UserIdentityDataPTO;
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AuthorizationApiFactoryTest
{
    private static final String SUCCESS_RESPONSE_ENVELOPE = "{ \"response\": { \"code\": \"SUCCESS\", \"data\": %s } })";

    private static final String ACCOUNT_BASED_USER_DETAILS_JSON =
            "{\"userIdentity\":{\"accountUid\":\"accountUid123\",\"projectUid\":null,\"agencyUid\":\"agencyUid123\",\"userUid\":\"userUid123\",\"roleName\":\"ROLE_AGENCY_OWNER\"}}";

    private static final String PROJECT_BASED_USER_DETAILS_JSON =
            "{\"userIdentity\":{\"accountUid\":\"accountUid123\",\"projectUid\":\"projectUid123\",\"agencyUid\":\"agencyUid123\",\"userUid\":\"userUid123\",\"roleName\":\"ROLE_AGENCY_OWNER\"}}";

    private MockWebServer mockWebServer;
    private AuthorizationApi authorizationApi;

    @Before
    public void setUp() throws Exception
    {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final AuthorizationApiFactory factory = new AuthorizationApiFactory();
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("foo");

        authorizationApi = factory.buildApi(tokenFilter, mockWebServer.url("/").toString());
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
    public void testGetAccountBaseUserIdentityDetails() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, PROJECT_BASED_USER_DETAILS_JSON));

        UserIdentityDataPTO identity = authorizationApi.getUserIdentity("projectUid123");

        final RecordedRequest request = mockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertTrue(request.getPath().contains("/authorization-api/v2/user-details/projects/projectUid123"));

        assertNotNull(identity);
        assertEquals("accountUid123", identity.getUserIdentity().getAccountUid());
        assertEquals("projectUid123", identity.getUserIdentity().getProjectUid());
        assertEquals("agencyUid123", identity.getUserIdentity().getAgencyUid());
        assertEquals("userUid123", identity.getUserIdentity().getUserUid());
        assertEquals("ROLE_AGENCY_OWNER", identity.getUserIdentity().getRoleName());
    }

    @Test
    public void testGetProjectBaseUserIdentityDetails() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, ACCOUNT_BASED_USER_DETAILS_JSON));

        UserIdentityDataPTO identity = authorizationApi.getUserIdentityByAccount("accountUid123");

        final RecordedRequest request = mockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertTrue(request.getPath().contains("/authorization-api/v2/user-details/accounts/accountUid123"));

        assertNotNull(identity);
        assertEquals("accountUid123", identity.getUserIdentity().getAccountUid());
        assertNull(identity.getUserIdentity().getProjectUid());
        assertEquals("agencyUid123", identity.getUserIdentity().getAgencyUid());
        assertEquals("userUid123", identity.getUserIdentity().getUserUid());
        assertEquals("ROLE_AGENCY_OWNER", identity.getUserIdentity().getRoleName());
    }
}