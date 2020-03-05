package com.smartling.api.files.v2;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.api.files.v2.pto.DownloadTranslationPTO;
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
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.smartling.api.files.v2.pto.RetrievalType.PUBLISHED;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;

public class FilesApiIntTest
{
    private final static String PROJECT_ID = "4bca2a7b8";
    private final static String FILE_URI = "file_uri.json";

    private FilesApi filesApi;

    @Rule
    public WireMockRule smartlingApi = new WireMockRule();

    @Before
    public void setUp() throws Exception
    {
        FilesApiFactory factory = new FilesApiFactory();
        BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("foo");
        ClientConfiguration config = DefaultClientConfiguration.builder()
            .baseUrl(new URL(smartlingApi.baseUrl()))
            .build();

        filesApi = factory.buildApi(tokenFilter, config);
    }

    @Test
    public void shouldRetrieveTranslation() throws Exception
    {
        String rawBody = UUID.randomUUID().toString();

        smartlingApi.stubFor(get(urlPathMatching("/files-api/v2/projects/.+/locales/zh-CN/file.*"))
            .withQueryParam("fileUri", equalTo(FILE_URI))
            .withQueryParam("retrievalType", equalTo(PUBLISHED.toString()))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(rawBody)
            )
        );

        InputStream response = filesApi.downloadTranslatedFile(
            PROJECT_ID,
            "zh-CN",
            DownloadTranslationPTO.builder()
                .fileUri(FILE_URI)
                .retrievalType(PUBLISHED)
                .build()
        );

        assertEquals(IOUtils.toString(response, UTF_8), rawBody);
    }

}
