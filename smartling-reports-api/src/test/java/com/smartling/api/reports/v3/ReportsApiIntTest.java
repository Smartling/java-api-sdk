package com.smartling.api.reports.v3;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.api.reports.v3.pto.WordCountReportCommandPTO;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;

public class ReportsApiIntTest
{
    @Rule
    public WireMockRule smartlingApi = new WireMockRule();

    private ReportsApi reportsApi;

    @Before
    public void setUp() throws Exception
    {
        ReportsApiFactory factory = new ReportsApiFactory();
        BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("foo");
        ClientConfiguration config = DefaultClientConfiguration.builder()
            .baseUrl(new URL(smartlingApi.baseUrl()))
            .build();

        reportsApi = factory.buildApi(tokenFilter, config);
    }

    @Test
    public void testDownloadWordCountReportInCsv() throws Exception
    {
        String rawBody = UUID.randomUUID().toString();

        smartlingApi.stubFor(post(urlPathMatching("/reports-api/v3/word-count/csv"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "text/csv")
                .withBody(rawBody)));

        InputStream response = reportsApi.downloadWordCountReport(
            WordCountReportCommandPTO.builder()
                .accountUid("a71308ec")
                .projectId("e240a7892")
                .startDate("20121-12-01")
                .endDate("20121-12-02")
                .limit(500)
                .build());

        assertEquals(IOUtils.toString(response, UTF_8), rawBody);
    }
}
