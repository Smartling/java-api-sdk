package com.smartling.api.mtrouter.v2;

import com.smartling.api.mtrouter.v2.pto.GenerateAccountTranslationCommandPTO;
import com.smartling.api.mtrouter.v2.pto.TranslationPTO;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.v2.response.ListResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;

import static com.smartling.api.mtrouter.v2.SampleApiResponses.ERRONEOUS_GENERATE_ACCOUNT_TRANSLATION_RESPONSE_BODY;
import static com.smartling.api.mtrouter.v2.SampleApiResponses.SUCCESS_GENERATE_ACCOUNT_TRANSLATION_RESPONSE_BODY;
import static com.smartling.api.mtrouter.v2.SampleApiResponses.SUCCESS_RESPONSE_ENVELOPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MtRouterApiTest
{
    private static final String ACCOUNT_UID = "a11223344";

    private MockWebServer mockWebServer;
    private MtRouterApi mtRouterApi;

    @Before
    public void setUp() throws Exception
    {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final MtRouterApiFactory factory = new MtRouterApiFactory();
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("foo");
        final ClientConfiguration config = DefaultClientConfiguration.builder().baseUrl(mockWebServer.url("/").url()).build();

        mtRouterApi = factory.buildApi(tokenFilter, config);
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
    public void testGenerateAccountTranslationErroneousResponse()
    {
        assignResponse(200, String.format(SUCCESS_RESPONSE_ENVELOPE, ERRONEOUS_GENERATE_ACCOUNT_TRANSLATION_RESPONSE_BODY));
        final GenerateAccountTranslationCommandPTO command = new GenerateAccountTranslationCommandPTO();

        ListResponse<TranslationPTO> response = mtRouterApi.generateAccountTranslations(ACCOUNT_UID, command);

        assertNotNull(response);
        assertEquals(1, response.getTotalCount());
        assertEquals(1, response.getItems().size());

        TranslationPTO translation = response.getItems().get(0);
        assertNotNull(translation);
        assertNull(translation.getMtUid());
        assertNull(translation.getTranslationText());
        assertNull(translation.getProvider());
        assertNotNull(translation.getError());
        assertEquals("test-key", translation.getKey());
        assertEquals("INVALID_SOURCE_TEXT", translation.getError().getCode());
        assertEquals("validation.error", translation.getError().getKey());
        assertEquals("Source text was invalid", translation.getError().getMessage());
    }

    @Test
    public void testGenerateAccountTranslationSuccessResponse()
    {
        assignResponse(200, String.format(SUCCESS_RESPONSE_ENVELOPE, SUCCESS_GENERATE_ACCOUNT_TRANSLATION_RESPONSE_BODY));
        final GenerateAccountTranslationCommandPTO command = new GenerateAccountTranslationCommandPTO();

        ListResponse<TranslationPTO> response = mtRouterApi.generateAccountTranslations(ACCOUNT_UID, command);

        assertNotNull(response);
        assertEquals(1, response.getTotalCount());
        assertEquals(1, response.getItems().size());

        TranslationPTO translation = response.getItems().get(0);
        assertNotNull(translation);
        assertEquals("test-key", translation.getKey());
        assertEquals("a36z4bunraj1", translation.getMtUid());
        assertEquals("Zu übersetzenden Text testen", translation.getTranslationText());
        assertNull(translation.getError());
        assertEquals("AUTO_SELECT_PROVIDER", translation.getProvider());
    }
}
