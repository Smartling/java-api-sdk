package com.smartling.api.files.v2;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.api.files.v2.pto.DownloadTranslationPTO;
import com.smartling.api.files.v2.pto.UploadFilePTO;
import com.smartling.api.files.v2.pto.UploadFileResponse;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aMultipart;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.smartling.api.v2.tests.wiremock.SmartlingWireMock.success;
import static com.smartling.api.files.v2.pto.FileType.JSON;
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

    @Test
    public void shouldUploadFile() throws Exception
    {
        String rawBody = UUID.randomUUID().toString();

        smartlingApi.stubFor(post(urlPathMatching("/files-api/v2/projects/.+/file"))
            // language=JSON
            .willReturn(success( "{\n" +
                    "    \"overWritten\": true,\n" +
                    "    \"stringCount\": 10,\n" +
                    "    \"wordCount\": 3,\n" +
                    "    \"message\" : \"uploaded\"\n" +
                    "}")
                )
            );

        Map<String, String> directives = new HashMap<>();
        directives.put("dir.name.1", "dirValue1");
        directives.put("dir.name.2", "dirValue2");

        UploadFileResponse uploadFileResponse = filesApi.uploadFile(PROJECT_ID, UploadFilePTO.builder()
            .authorize(true)
            .fileType(JSON)
            .fileUri(FILE_URI)
            .file(new ByteArrayInputStream(rawBody.getBytes()))
            .directives(directives)
            .localeIdsToAuthorize(Arrays.asList("de-DE", "fr"))
            .build());

        smartlingApi.verify(postRequestedFor(urlEqualTo("/files-api/v2/projects/" + PROJECT_ID + "/file"))
            .withRequestBodyPart(aMultipart()
                .withName("dir.name.1")
                .withBody(equalTo("dirValue1"))
                .build())
            .withRequestBodyPart(aMultipart()
                .withName("dir.name.2")
                .withBody(equalTo("dirValue2"))
                .build())
            .withRequestBodyPart(aMultipart()
                .withName("fileUri")
                .withBody(equalTo(FILE_URI))
                .build())
            .withRequestBodyPart(aMultipart()
                .withName("authorize")
                .withBody(equalTo("true"))
                .build())
            .withRequestBodyPart(aMultipart()
                .withName("fileType")
                .withBody(equalTo("JSON"))
                .build())
            .withRequestBodyPart(aMultipart()
                .withName("localeIdsToAuthorize[]")
                .withBody(equalTo("de-DE"))
                .build())
            .withRequestBodyPart(aMultipart()
                .withName("localeIdsToAuthorize[]")
                .withBody(equalTo("fr"))
                .build())
            .withRequestBodyPart(aMultipart()
                .withName("file")
                .withBody(equalTo(rawBody))
                .build())
        );
    }
}
