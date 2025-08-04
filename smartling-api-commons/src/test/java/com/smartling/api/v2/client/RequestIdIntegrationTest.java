package com.smartling.api.v2.client;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.v2.client.exception.RestApiRuntimeException;
import com.smartling.api.v2.client.exception.client.ValidationErrorException;
import lombok.Data;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ResponseProcessingException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Collections;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class RequestIdIntegrationTest
{
    private static final String EXTERNAL_REQUEST_ID = "external-request-id";
    private static final String DEFAULT_REQUEST_ID_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

    private interface TestTimeoutApi
    {
        @GET
        @Path("/test/success")
        String getQuickResponse();

        @GET
        @Path("/test/error")
        String getErrorResponse();

        @GET
        @Path("/test/processing-error")
        DTO getProcessingErrorResponse();

        @GET
        @Path("/test/timeout")
        String getSlowResponse();

        @Data
        class DTO
        {
            private String data;
        }
    }

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(0);

    private TestTimeoutApi testApi;

    @Before
    public void setUp() throws IOException
    {
        stubFor(get(urlEqualTo("/test/success"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"response\":{\"code\":\"SUCCESS\",\"data\":\"slow-response\"}}")));

        stubFor(get(urlEqualTo("/test/error"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader("X-SL-RequestId", EXTERNAL_REQUEST_ID)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"response\":{\"code\":\"VALIDATION_ERROR\",\"errors\":[{\"message\":\"Bad request\"}]}}")));

        stubFor(get(urlEqualTo("/test/processing-error"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("X-SL-RequestId", EXTERNAL_REQUEST_ID)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"response\":{\"code\":\"SUCCESS\",\"data\":\"raw string\"}}")));

        stubFor(get(urlEqualTo("/test/timeout"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"response\":{\"code\":\"SUCCESS\",\"data\":\"slow-response\"}}")
                .withFixedDelay(2000)));
    }

    @Test
    public void shouldSendDefaultRequestId()
    {
        testApi = new ClientFactory().build(
            Collections.singletonList(new BearerAuthStaticTokenFilter("test-token")),
            Collections.emptyList(),
            "http://localhost:" + wireMockRule.port(),
            TestTimeoutApi.class,
            new HttpClientConfiguration(),
            null,
            null
        );

        testApi.getQuickResponse();

        verify(getRequestedFor(urlEqualTo("/test/success"))
            .withHeader("X-SL-RequestId", matching(DEFAULT_REQUEST_ID_PATTERN)));
    }

    @Test
    public void shouldSendRequestId()
    {
        testApi = new ClientFactory().build(
            Arrays.asList(new BearerAuthStaticTokenFilter("test-token"), new ExternalRequestIdFilter()),
            Collections.emptyList(),
            "http://localhost:" + wireMockRule.port(),
            TestTimeoutApi.class,
            new HttpClientConfiguration(),
            null,
            null
        );

        testApi.getQuickResponse();

        verify(getRequestedFor(urlEqualTo("/test/success"))
            .withHeader("X-SL-RequestId", equalTo(EXTERNAL_REQUEST_ID)));
    }

    @Test
    public void shouldUseRequestIdFromErrorResponse()
    {
        testApi = new ClientFactory().build(
            Collections.singletonList(new BearerAuthStaticTokenFilter("test-token")),
            Collections.emptyList(),
            "http://localhost:" + wireMockRule.port(),
            TestTimeoutApi.class,
            new HttpClientConfiguration(),
            null,
            null
        );

        try
        {
            testApi.getErrorResponse();
            fail("Expected 400 error");
        }
        catch (RestApiRuntimeException e)
        {
            String expectedMessage = format("http_status=400, requestId=%s, top errors: 'Bad request'", EXTERNAL_REQUEST_ID);
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(ValidationErrorException.class, e.getClass());
        }
    }

    @Test
    public void shouldUseRequestIdFromErrorResponseOnBodyReadFailure()
    {
        testApi = new ClientFactory().build(
            Collections.singletonList(new BearerAuthStaticTokenFilter("test-token")),
            Collections.emptyList(),
            "http://localhost:" + wireMockRule.port(),
            TestTimeoutApi.class,
            new HttpClientConfiguration(),
            null,
            null
        );

        try
        {
            testApi.getProcessingErrorResponse();
            fail("Expected deserialization error");
        }
        catch (RestApiRuntimeException e)
        {
            String expectedMessage = format("http_status=200, requestId=%s", EXTERNAL_REQUEST_ID);
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(ResponseProcessingException.class, e.getCause().getClass());
        }
    }

    @Test
    public void shouldPreservesRequestIdOnReadTimeout()
    {
        testApi = new ClientFactory().build(
            Arrays.asList(new BearerAuthStaticTokenFilter("test-token"), new ExternalRequestIdFilter()),
            Collections.emptyList(),
            "http://localhost:" + wireMockRule.port(),
            TestTimeoutApi.class,
            new HttpClientConfiguration().setSocketTimeout(1000),
            null,
            null
        );

        try
        {
            testApi.getSlowResponse();
            fail("Expected timeout exception");
        }
        catch (RestApiRuntimeException e)
        {
            assertEquals("http_status=500, requestId=" + EXTERNAL_REQUEST_ID, e.getMessage());
            assertEquals(ProcessingException.class, e.getCause().getClass());
            assertEquals(SocketTimeoutException.class, e.getCause().getCause().getClass());
        }
    }

    private static class ExternalRequestIdFilter implements ClientRequestFilter
    {
        @Override
        public void filter(ClientRequestContext requestContext)
        {
            requestContext.getHeaders().add("X-SL-RequestId", EXTERNAL_REQUEST_ID);
        }
    }
}
