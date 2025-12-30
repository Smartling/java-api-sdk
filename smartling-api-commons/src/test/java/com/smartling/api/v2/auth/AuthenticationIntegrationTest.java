package com.smartling.api.v2.auth;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.smartling.api.v2.authentication.AuthenticationApi;
import com.smartling.api.v2.authentication.AuthenticationApiFactory;
import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ApiFactory;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.ClientFactory;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.Authenticator;
import com.smartling.api.v2.client.auth.BearerAuthSecretFilter;
import com.smartling.api.v2.client.exception.RestApiRuntimeException;
import com.smartling.api.v2.client.exception.server.MaintanenceModeErrorException;
import com.smartling.api.v2.client.exception.server.ServerApiException;
import com.smartling.api.v2.response.ResponseCode;
import com.smartling.api.v2.tests.wiremock.SmartlingWireMock;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.URL;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.smartling.api.v2.auth.DummyApi.DUMMY_API;
import static com.smartling.api.v2.tests.wiremock.SmartlingWireMock.*;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AuthenticationIntegrationTest
{
    @Rule
    public WireMockRule smartlingApi = new WireMockRule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final ApiFactory<DummyApi> dummyFactory = new AbstractApiFactory<DummyApi>()
    {
        @Override
        protected Class<DummyApi> getApiClass()
        {
            return DummyApi.class;
        }
    };

    @Before
    public void setup()
    {
        smartlingApi.stubFor(get(urlEqualTo(DUMMY_API)).willReturn(ok()));
    }

    private DummyApi dummyApi() throws Exception
    {
        return dummyApi("user", "secret");
    }

    private DummyApi dummyApi(String user, String secret) throws Exception
    {
        ClientFactory clientFactory = new ClientFactory();
        ClientConfiguration configuration = DefaultClientConfiguration.builder()
            .baseUrl(new URL(smartlingApi.baseUrl()))
            .build();
        AuthenticationApi authenticationApi = new AuthenticationApiFactory(clientFactory)
            .buildApi(configuration);
        Authenticator authenticator = new Authenticator(user, secret, authenticationApi);

        return dummyFactory.buildApi(new BearerAuthSecretFilter(authenticator), configuration);
    }

    @Test
    public void shouldAuthenticate() throws Exception
    {
        smartlingApi.stubFor(postJson(urlEqualTo("/auth-api/v2/authenticate"))
            .withRequestBody(equalToJson("{\n" +
                "  \"userIdentifier\": \"user\",\n" +
                "  \"userSecret\": \"secret\"\n" +
                "}")
            )
            // language=JSON
            .willReturn(success("{\n" +
                "  \"accessToken\": \"accessTokenValue\",\n" +
                "  \"refreshToken\": \"refreshTokenValue\",\n" +
                "  \"expiresIn\": 480,\n" +
                "  \"refreshExpiresIn\": 21600,\n" +
                "  \"tokenType\": \"Bearer\"\n" +
                "}")
            )
        );

        dummyApi().dummy();

        smartlingApi.verify(getRequestedFor(urlEqualTo(DUMMY_API))
            .withHeader("Authorization", equalTo("Bearer accessTokenValue")));
        smartlingApi.verify(postRequestedFor(urlEqualTo("/auth-api/v2/authenticate"))
            .withHeader(HttpHeaders.USER_AGENT, matching("smartling-api-commons-java/.*")));
    }

    @Test
    public void shouldLogWeirdContentType()
    {
        // language=JSON
        String data = "{\n" +
            "  \"accessToken\": \"accessTokenValue\",\n" +
            "  \"refreshToken\": \"refreshTokenValue\",\n" +
            "  \"expiresIn\": 480,\n" +
            "  \"refreshExpiresIn\": 21600,\n" +
            "  \"tokenType\": \"Bearer\"\n" +
            "}";
        StubMapping stubMapping = smartlingApi.stubFor(postJson(urlEqualTo("/auth-api/v2/authenticate"))
            .withRequestBody(equalToJson("{\n" +
                "  \"userIdentifier\": \"user\",\n" +
                "  \"userSecret\": \"secret\"\n" +
                "}")
            )
            .willReturn(smartlingResponse(SmartlingWireMock.ResponseCode.SUCCESS, "*/*", data, null)
            )
        );
        String actualMessage = null;
        try
        {
            dummyApi().dummy();
        }
        catch (Exception ex) {
            assertNotNull("Expected exception to have a cause", ex.getCause());
            assertNotNull("Expected cause to have diagnostic message", ex.getCause().getCause());
            actualMessage = ex.getCause().getCause().getMessage();
        }

        // Verify diagnostic message contains all critical information (platform-independent assertions)
        assertNotNull("Expected error message", actualMessage);
        assertTrue("Should contain error processing header",
            actualMessage.contains("Error during response processing:"));
        assertTrue("Should contain type information",
            actualMessage.contains("type: com.smartling.api.v2.authentication.pto.Authentication"));
        assertTrue("Should contain genericType information",
            actualMessage.contains("genericType: class com.smartling.api.v2.authentication.pto.Authentication"));
        assertTrue("Should contain POST annotation",
            actualMessage.contains("@javax.ws.rs.POST()"));
        assertTrue("Should contain Path annotation with authenticate path",
            actualMessage.contains("@javax.ws.rs.Path") && actualMessage.contains("/authenticate"));
        assertTrue("Should contain Content-Type header",
            actualMessage.contains("Content-Type=*/*"));
        assertTrue("Should contain stub ID",
            actualMessage.contains("Matched-Stub-Id=" + stubMapping.getId()));
        assertTrue("Should contain media type",
            actualMessage.contains("media type: */*"));
        assertTrue("Should contain response body with success code",
            actualMessage.contains("\"code\":\"SUCCESS\"") &&
            actualMessage.contains("\"accessToken\":\"accessTokenValue\""));
    }

    @Test
    public void shouldRefreshToken() throws Exception
    {
        smartlingApi.stubFor(postJson(urlEqualTo("/auth-api/v2/authenticate"))
            .withRequestBody(equalToJson("{\n" +
                "  \"userIdentifier\": \"user\",\n" +
                "  \"userSecret\": \"secret\"\n" +
                "}")
            )
            // language=JSON
            .willReturn(success("{\n" +
                "  \"accessToken\": \"accessTokenValue\",\n" +
                "  \"refreshToken\": \"refreshTokenValue\",\n" +
                "  \"expiresIn\": 1,\n" +
                "  \"refreshExpiresIn\": 21600,\n" +
                "  \"tokenType\": \"Bearer\"\n" +
                "}")
            )
        );

        smartlingApi.stubFor(postJson(urlEqualTo("/auth-api/v2/authenticate/refresh"))
            .withRequestBody(equalToJson("{\n" +
                "  \"refreshToken\": \"refreshTokenValue\"\n" +
                "}")
            )
            // language=JSON
            .willReturn(success("{\n" +
                "  \"accessToken\": \"refreshedAccessTokenValue\",\n" +
                "  \"refreshToken\": \"refreshedRefreshTokenValue\",\n" +
                "  \"expiresIn\": 1,\n" +
                "  \"refreshExpiresIn\": 21600,\n" +
                "  \"tokenType\": \"Bearer\"\n" +
                "}")
            )
        );

        DummyApi dummyApi = dummyApi();
        dummyApi.dummy();

        smartlingApi.verify(1, getRequestedFor(urlEqualTo(DUMMY_API))
            .withHeader("Authorization", equalTo("Bearer accessTokenValue")));

        sleep(1001);
        dummyApi.dummy();

        smartlingApi.verify(1, getRequestedFor(urlEqualTo(DUMMY_API))
            .withHeader("Authorization", equalTo("Bearer refreshedAccessTokenValue")));

        smartlingApi.verify(postRequestedFor(urlEqualTo("/auth-api/v2/authenticate"))
            .withHeader(HttpHeaders.USER_AGENT, matching("smartling-api-commons-java/.*")));
        smartlingApi.verify(postRequestedFor(urlEqualTo("/auth-api/v2/authenticate/refresh"))
            .withHeader(HttpHeaders.USER_AGENT, matching("smartling-api-commons-java/.*")));
    }

    @Test
    public void shouldThrowAuthenticationErrorExceptionOnInvalidCredentials() throws Exception
    {
        smartlingApi.stubFor(postJson(urlEqualTo("/auth-api/v2/authenticate"))
            .withRequestBody(equalToJson("{\n" +
                "  \"userIdentifier\": \"invalidUser\",\n" +
                "  \"userSecret\": \"invalidSecret\"\n" +
                "}")
            )
            .willReturn(error(ResponseCode.VALIDATION_ERROR, "[\n" +
                    "  {\n" +
                    "    \"key\": null,\n" +
                    "    \"message\": \"HTTP 401 Unauthorized\",\n" +
                    "    \"details\": null\n" +
                    "  }\n" +
                    "]")
                .withStatus(HttpStatus.SC_UNAUTHORIZED)
            )
        );

        thrown.expect(new ExceptionMatcher(HttpStatus.SC_UNAUTHORIZED, "http_status=401, top errors: 'HTTP 401 Unauthorized'"));

        dummyApi("invalidUser", "invalidSecret").dummy();

        smartlingApi.verify(0, getRequestedFor(urlEqualTo(DUMMY_API)));
    }

    @Test
    public void shouldThrowServerApiException() throws Exception
    {
        smartlingApi.stubFor(postJson(urlEqualTo("/auth-api/v2/authenticate"))
            .willReturn(error(ResponseCode.GENERAL_ERROR, "[]")
                .withStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR)
            )
        );

        thrown.expect(ServerApiException.class);

        dummyApi().dummy();

        smartlingApi.verify(0, getRequestedFor(urlEqualTo(DUMMY_API)));
    }

    @Test
    public void shouldThrowMaintenanceModeErrorException() throws Exception
    {
        smartlingApi.stubFor(postJson(urlEqualTo("/auth-api/v2/authenticate"))
            .willReturn(error(ResponseCode.MAINTENANCE_MODE_ERROR, "[]")
                .withStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR)
            )
        );

        thrown.expect(MaintanenceModeErrorException.class);

        dummyApi().dummy();

        smartlingApi.verify(0, getRequestedFor(urlEqualTo(DUMMY_API)));
    }

    @Test
    public void shouldThrowGenericRuntimeException() throws Exception
    {
        smartlingApi.stubFor(postJson(urlEqualTo("/auth-api/v2/authenticate"))
            .willReturn(serverError()));

        thrown.expect(RestApiRuntimeException.class);

        dummyApi().dummy();

        smartlingApi.verify(0, getRequestedFor(urlEqualTo(DUMMY_API)));
    }

    public class ExceptionMatcher extends BaseMatcher<RestApiRuntimeException>
    {
        private final int status;
        private final String message;

        public ExceptionMatcher(int status, String message)
        {

            this.status = status;
            this.message = message;
        }

        @Override
        public boolean matches(Object o)
        {
            RestApiRuntimeException ex = (RestApiRuntimeException)o;

            return ex.getStatus() == this.status && ex.getMessage().equals(this.message);
        }

        @Override
        public void describeTo(Description description)
        {

        }
    }
}
