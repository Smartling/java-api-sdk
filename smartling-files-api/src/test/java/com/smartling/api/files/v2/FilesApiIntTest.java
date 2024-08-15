package com.smartling.api.files.v2;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.api.files.v2.pto.DownloadTranslationPTO;
import com.smartling.api.files.v2.pto.FileLocaleLastModifiedPTO;
import com.smartling.api.files.v2.pto.FileLocaleStatusResponse;
import com.smartling.api.files.v2.pto.GetFileLastModifiedPTO;
import com.smartling.api.files.v2.pto.UploadFilePTO;
import com.smartling.api.files.v2.pto.UploadFileResponse;
import com.smartling.api.files.v2.resteasy.ext.TranslatedFileMultipart;
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
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.smartling.api.v2.tests.wiremock.SmartlingWireMock.success;
import static com.smartling.api.files.v2.pto.FileType.JSON;
import static com.smartling.api.files.v2.pto.RetrievalType.PUBLISHED;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;

public class FilesApiIntTest
{
    private final static String PROJECT_ID = "4bca2a7b8";
    private final static String FILE_URI = "Formatting content 🎨 йцукенг 中国象形文字";

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
    public void shouldRetrieveTranslationMultipart() throws Exception
    {
        final String boundary = UUID.randomUUID().toString();
        final String responseBody = "--" + boundary + "\r\n"
            + "Content-Type: application/octet-stream; charset=UTF-8\r\n"
            + "Content-Disposition: attachment; filename=\"myfile.properties\";\r\n"
            + "\r\n"
            + "key1=value1\nkey2=value2\r\n"
            + "--" + boundary + "\r\n"
            + "Content-Type: application/json; charset=UTF-8\r\n"
            + "\r\n"
            + "{\"key\":\"value\"}\r\n"
            + "--" + boundary + "--";

        smartlingApi.stubFor(get(urlPathMatching("/files-api/v2/projects/.+/locales/zh-CN/file.*"))
            .withQueryParam("fileUri", equalTo(FILE_URI))
            .withQueryParam("retrievalType", equalTo(PUBLISHED.toString()))
            .willReturn(aResponse()
                .withHeader("Content-Type", "multipart/mixed; boundary=" + boundary)
                .withBody(responseBody)
            )
        );

        TranslatedFileMultipart response = filesApi.downloadTranslatedFileMultipart(
            PROJECT_ID,
            "zh-CN",
            DownloadTranslationPTO.builder()
                .fileUri(FILE_URI)
                .retrievalType(PUBLISHED)
                .build()
        );

        InputStream stream = response.getFileBody();

        assertEquals(IOUtils.toString(stream, UTF_8), "key1=value1\nkey2=value2");
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
                    "    \"fileUri\": \"" + FILE_URI + "\",\n" +
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

        assertEquals(uploadFileResponse.getFileUri(), FILE_URI);

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

    @Test
    public void shouldGetLastModified() throws Exception
    {
        String locale = "fr-FR";

        smartlingApi.stubFor(get(urlPathMatching("/files-api/v2/projects/" + PROJECT_ID + "/locales/" + locale + "/file/last-modified"))
            // language=JSON
            .willReturn(success( "{\n" +
                "    \"localeId\": \"fr-FR\",\n" +
                "    \"lastModified\": \"2018-07-21T00:56:35Z\"\n" +
                "}")
            )
        );

        FileLocaleLastModifiedPTO fileLocaleLastModified = filesApi.getFileLocaleLastModified(PROJECT_ID, "fr-FR",
            GetFileLastModifiedPTO.builder()
                .fileUri(FILE_URI)
                .build());

        smartlingApi.verify(getRequestedFor(urlPathEqualTo("/files-api/v2/projects/" + PROJECT_ID + "/locales/fr-FR/file/last-modified"))
            .withQueryParam("fileUri", equalTo(FILE_URI))
        );
    }

    @Test
    public void shouldGetFileLocaleStatus() throws Exception
    {
        String locale = "fr-FR";

        smartlingApi.stubFor(get(urlPathMatching("/files-api/v2/projects/" + PROJECT_ID + "/locales/" + locale + "/file/status"))
            // language=JSON
            .willReturn(success( "" +
                "{\n" +
                "    \"fileUri\": \"" + FILE_URI + "\",\n" +
                "    \"lastUploaded\": \"2018-07-21T00:56:35Z\",\n" +
                "    \"created\": \"2018-07-21T00:56:34Z\",\n" +
                "    \"fileType\": \"xml\",\n" +
                "    \"parserVersion\": 4,\n" +
                "    \"hasInstructions\": true,\n" +
                "    \"directives\": {\n" +
                "        \"namespace\": \"smt-test-page\",\n" +
                "        \"client_lib_id\": \"{\\\"client\\\":\\\"hybris-connector\\\",\\\"version\\\":\\\"2.0.8-2-ge785e88f\\\"}\"\n" +
                "    },\n" +
                "    \"namespace\": {\n" +
                "        \"name\": \"smt-test-page\"\n" +
                "    },\n" +
                "    \"totalStringCount\": 6,\n" +
                "    \"totalWordCount\": 20,\n" +
                "    \"localeId\": \"de-DE\",\n" +
                "    \"authorizedStringCount\": 10,\n" +
                "    \"authorizedWordCount\": 12,\n" +
                "    \"completedStringCount\": 5,\n" +
                "    \"completedWordCount\": 15,\n" +
                "    \"excludedStringCount\": 1,\n" +
                "    \"excludedWordCount\": 11\n" +
                "}\n"
                )
            )
        );

        FileLocaleStatusResponse fileLocaleStatus = filesApi.getFileLocaleStatus(PROJECT_ID, locale, FILE_URI);

        smartlingApi.verify(getRequestedFor(urlPathEqualTo("/files-api/v2/projects/" + PROJECT_ID + "/locales/" + locale + "/file/status"))
            .withQueryParam("fileUri", equalTo(FILE_URI))
        );
    }
}
