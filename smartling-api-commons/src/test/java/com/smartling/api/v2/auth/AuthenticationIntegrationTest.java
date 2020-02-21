package com.smartling.api.v2.auth;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
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
import com.smartling.api.v2.client.exception.client.AuthenticationErrorException;
import com.smartling.api.v2.client.exception.server.MaintanenceModeErrorException;
import com.smartling.api.v2.client.exception.server.ServerApiException;
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
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.unauthorized;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.smartling.api.v2.auth.DummyApi.DUMMY_API;

public class AuthenticationIntegrationTest
{
    @Rule
    public WireMockRule smartlingApi = new WireMockRule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ApiFactory<DummyApi> dummyFactory = new AbstractApiFactory<DummyApi>()
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
        smartlingApi.stubFor(post(urlEqualTo("/auth-api/v2/authenticate"))
            .withHeader("Content-Type", equalTo("application/json"))
            .withRequestBody(equalToJson("{\n" +
                "  \"userIdentifier\": \"user\",\n" +
                "  \"userSecret\": \"secret\"\n" +
                "}")
            )
            // language=JSON
            .willReturn(okJson("{\n" +
                "  \"response\": {\n" +
                "    \"code\": \"SUCCESS\",\n" +
                "    \"data\": {\n" +
                "      \"accessToken\": \"accessTokenValue\",\n" +
                "      \"refreshToken\": \"refreshTokenValue\",\n" +
                "      \"expiresIn\": 480,\n" +
                "      \"refreshExpiresIn\": 21600,\n" +
                "      \"tokenType\": \"Bearer\"\n" +
                "    }\n" +
                "  }\n" +
                "}")
            )
        );

        dummyApi().dummy();

        smartlingApi.verify(getRequestedFor(urlEqualTo(DUMMY_API))
            .withHeader("Authorization", equalTo("Bearer accessTokenValue")));
    }

    @Test
    public void shouldThrowValidationErrorExceptionOnInvalidCredentials() throws Exception
    {
        smartlingApi.stubFor(post(urlEqualTo("/auth-api/v2/authenticate"))
            .withHeader("Content-Type", equalTo("application/json"))
            .withRequestBody(equalToJson("{\n" +
                "  \"userIdentifier\": \"invalidUser\",\n" +
                "  \"userSecret\": \"invalidSecret\"\n" +
                "}")
            )
            .willReturn(unauthorized()
                .withHeader("Content-Type", "application/json")
                // language=JSON
                .withBody("{\n" +
                    "  \"response\": {\n" +
                    "    \"code\": \"VALIDATION_ERROR\",\n" +
                    "    \"errors\": [\n" +
                    "      {\n" +
                    "        \"key\": null,\n" +
                    "        \"message\": \"HTTP 401 Unauthorized\",\n" +
                    "        \"details\": null\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}\n")
            )
        );

        thrown.expect(AuthenticationErrorException.class);

        dummyApi("invalidUser", "invalidSecret").dummy();

        smartlingApi.verify(0, getRequestedFor(urlEqualTo(DUMMY_API)));
    }

    @Test
    public void shouldThrowServerApiException() throws Exception
    {
        smartlingApi.stubFor(post(urlEqualTo("/auth-api/v2/authenticate"))
            .willReturn(serverError()
            .withHeader("Content-Type", "application/json")
            // language=JSON
            .withBody("{\n" +
                "  \"response\": {\n" +
                "    \"code\": \"GENERAL_ERROR\",\n" +
                "    \"errors\": []\n" +
                "  }\n" +
                "}")
            )
        );

        thrown.expect(ServerApiException.class);

        dummyApi().dummy();

        smartlingApi.verify(0, getRequestedFor(urlEqualTo(DUMMY_API)));
    }

    @Test
    public void shouldThrowMaintanenceModeErrorException() throws Exception
    {
        smartlingApi.stubFor(post(urlEqualTo("/auth-api/v2/authenticate"))
            .willReturn(serverError()
                .withHeader("Content-Type", "application/json")
                // language=JSON
                .withBody("{\n" +
                    "  \"response\": {\n" +
                    "    \"code\": \"MAINTENANCE_MODE_ERROR\",\n" +
                    "    \"errors\": []\n" +
                    "  }\n" +
                    "}")
            )
        );

        thrown.expect(MaintanenceModeErrorException.class);

        dummyApi().dummy();

        smartlingApi.verify(0, getRequestedFor(urlEqualTo(DUMMY_API)));
    }


    @Test
    public void shouldThrowGenericRuntimeException() throws Exception
    {
        smartlingApi.stubFor(post(urlEqualTo("/auth-api/v2/authenticate"))
            .willReturn(serverError()));

        thrown.expect(RestApiRuntimeException.class);

        dummyApi().dummy();

        smartlingApi.verify(0, getRequestedFor(urlEqualTo(DUMMY_API)));
    }
}
