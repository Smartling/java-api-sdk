package com.smartling.api.files.v2;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.smartling.api.files.v2.pto.DownloadTranslationPTO;
import com.smartling.api.files.v2.pto.FileItemPTO;
import com.smartling.api.files.v2.pto.FileLocaleStatusResponse;
import com.smartling.api.files.v2.pto.FileStatusResponse;
import com.smartling.api.files.v2.pto.FileType;
import com.smartling.api.files.v2.pto.GetFilesListPTO;
import com.smartling.api.files.v2.pto.GetRecentlyPublishedFilesPTO;
import com.smartling.api.files.v2.pto.OrderBy;
import com.smartling.api.files.v2.pto.RecentlyPublishedFileItemPTO;
import com.smartling.api.files.v2.pto.RetrievalType;
import com.smartling.api.files.v2.pto.UploadFilePTO;
import com.smartling.api.files.v2.pto.UploadFileResponse;
import com.smartling.api.files.v2.resteasy.ext.TranslatedFileMultipart;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.v2.client.exception.RestApiRuntimeException;
import com.smartling.api.v2.response.ListResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static com.smartling.api.files.v2.pto.FileType.JSON;
import static java.util.Collections.singletonList;
import static org.jboss.resteasy.plugins.providers.multipart.MultipartConstants.MULTIPART_MIXED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FilesApiTest
{
    private final static SimpleDateFormat FAPI_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.ENGLISH); //2018-07-21T00:56:35Z
    private final static String SUCCESS_RESPONSE_ENVELOPE = "{\"response\":{\"code\":\"SUCCESS\",\"data\":%s}})";

    private final static String PROJECT_ID = "4bca2a7b8";
    private final static String FILE_URI = "file_uri.json";

    private MockWebServer mockWebServer;
    private FilesApi filesApi;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Before
    public void setUp() throws Exception
    {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final FilesApiFactory factory = new FilesApiFactory();
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("foo");
        final ClientConfiguration config = DefaultClientConfiguration.builder().baseUrl(mockWebServer.url("/").url()).build();

        filesApi = factory.buildApi(tokenFilter, config);
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
    public void testGetFileStatus() throws Exception
    {
        // language=JSON
        String getFileStatusResponseBody = "" +
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
            "    \"totalCount\": 1,\n" +
            "    \"items\": [\n" +
            "        {\n" +
            "            \"localeId\": \"de-DE\",\n" +
            "            \"authorizedStringCount\": 10,\n" +
            "            \"authorizedWordCount\": 12,\n" +
            "            \"completedStringCount\": 5,\n" +
            "            \"completedWordCount\": 15,\n" +
            "            \"excludedStringCount\": 1,\n" +
            "            \"excludedWordCount\": 11\n" +
            "        }\n" +
            "    ]\n" +
            "}\n";
        assignResponse(200, String.format(SUCCESS_RESPONSE_ENVELOPE, getFileStatusResponseBody));

        FileStatusResponse response = filesApi.getFileStatus(PROJECT_ID, FILE_URI);

        assertEquals(date("2018-07-21T00:56:34Z"), response.getCreated());
        assertEquals("xml", response.getFileType());
        assertEquals(FILE_URI, response.getFileUri());
        assertTrue(response.isHasInstructions());
        assertEquals(date("2018-07-21T00:56:35Z"), response.getLastUploaded());
        assertNotNull(response.getNamespace());
        assertEquals("smt-test-page", response.getNamespace().getName());
        assertNotNull(response.getDirectives());
        assertEquals("smt-test-page", response.getDirectives().get("namespace"));
        assertEquals("{\"client\":\"hybris-connector\",\"version\":\"2.0.8-2-ge785e88f\"}", response.getDirectives().get("client_lib_id"));
        assertEquals(4, response.getParserVersion());
        assertEquals(1, response.getTotalCount());
        assertEquals(6, response.getTotalStringCount());
        assertEquals(20, response.getTotalWordCount());
        assertEquals(1, response.getItems().size());
        assertEquals("de-DE", response.getItems().get(0).getLocaleId());
        assertEquals(10, response.getItems().get(0).getAuthorizedStringCount());
        assertEquals(12, response.getItems().get(0).getAuthorizedWordCount());
        assertEquals(5, response.getItems().get(0).getCompletedStringCount());
        assertEquals(15, response.getItems().get(0).getCompletedWordCount());
        assertEquals(1, response.getItems().get(0).getExcludedStringCount());
        assertEquals(11, response.getItems().get(0).getExcludedWordCount());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertTrue(recordedRequest.getPath().contains("/projects/" + PROJECT_ID + "/file/status"));
    }

    @Test
    public void testGetFileLocaleStatus() throws Exception
    {
        // language=JSON
        String getFileLocaleStatusResponseBody = "" +
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
            "}\n";
        assignResponse(200, String.format(SUCCESS_RESPONSE_ENVELOPE, getFileLocaleStatusResponseBody));

        FileLocaleStatusResponse response = filesApi.getFileLocaleStatus(PROJECT_ID, "de-DE", FILE_URI);

        assertEquals(date("2018-07-21T00:56:34Z"), response.getCreated());
        assertEquals("xml", response.getFileType());
        assertEquals(FILE_URI, response.getFileUri());
        assertTrue(response.isHasInstructions());
        assertNotNull(response.getNamespace());
        assertEquals("smt-test-page", response.getNamespace().getName());
        assertNotNull(response.getDirectives());
        assertEquals("smt-test-page", response.getDirectives().get("namespace"));
        assertEquals("{\"client\":\"hybris-connector\",\"version\":\"2.0.8-2-ge785e88f\"}", response.getDirectives().get("client_lib_id"));
        assertEquals(date("2018-07-21T00:56:35Z"), response.getLastUploaded());
        assertEquals(4, response.getParserVersion());
        assertEquals(6, response.getTotalStringCount());
        assertEquals(20, response.getTotalWordCount());
        assertEquals(10, response.getAuthorizedStringCount());
        assertEquals(12, response.getAuthorizedWordCount());
        assertEquals(5, response.getCompletedStringCount());
        assertEquals(15, response.getCompletedWordCount());
        assertEquals(1, response.getExcludedStringCount());
        assertEquals(11, response.getExcludedWordCount());
    }

    @Test
    public void testGetFilesList() throws Exception
    {
        // language=JSON
        String getFilesListResponseBody = "" +
            "{\n" +
            "    \"items\": [\n" +
            "        {\n" +
            "            \"fileUri\": \"" + FILE_URI + "\",\n" +
            "            \"lastUploaded\": \"2018-07-21T00:56:35Z\",\n" +
            "            \"created\": \"2018-07-21T00:56:34Z\",\n" +
            "            \"fileType\": \"xml\",\n" +
            "            \"hasInstructions\": false\n" +
            "        }\n" +
            "    ],\n" +
            "    \"totalCount\": 1\n" +
            "}\n";
        assignResponse(200, String.format(SUCCESS_RESPONSE_ENVELOPE, getFilesListResponseBody));

        ListResponse<FileItemPTO> response = filesApi.getFilesList(
                PROJECT_ID,
                GetFilesListPTO.builder()
                    .fileTypes(Arrays.asList(FileType.XML, FileType.JSON))
                    .uriMask("uri*mask.json")
                    .lastUploadedAfter("2018-01-01T00:56:34Z")
                    .lastUploadedBefore("2019-01-01T00:56:34Z")
                    .limit(10)
                    .offset(100)
                    .orderBy(OrderBy.LAST_UPLOADED_DESC)
                .build());

        assertEquals(1, response.getItems().size());
        assertEquals(FILE_URI, response.getItems().get(0).getFileUri());
        assertEquals(date("2018-07-21T00:56:34Z"), response.getItems().get(0).getCreated());
        assertEquals(date("2018-07-21T00:56:35Z"), response.getItems().get(0).getLastUploaded());
        assertEquals("xml", response.getItems().get(0).getFileType());
        assertFalse(response.getItems().get(0).isHasInstructions());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertTrue(recordedRequest.getPath().contains("/projects/" + PROJECT_ID + "/files/list"));
        assertTrue(recordedRequest.getPath().contains("uriMask=uri*mask.json"));
        assertTrue(recordedRequest.getPath().contains("lastUploadedAfter=" + URLEncoder.encode("2018-01-01T00:56:34Z")));
        assertTrue(recordedRequest.getPath().contains("lastUploadedBefore=" + URLEncoder.encode("2019-01-01T00:56:34Z")));
        assertTrue(recordedRequest.getPath().contains("limit=10"));
        assertTrue(recordedRequest.getPath().contains("offset=100"));
        assertTrue(recordedRequest.getPath().contains("fileTypes=XML"));
        assertTrue(recordedRequest.getPath().contains("fileTypes=JSON"));
    }

    @Test
    public void testGetRecentlyPublishedFilesList() throws Exception {
        String getRecentlyPublishedFilesListResponseBody = "" +
            "{\n" +
            "    \"items\": [\n" +
            "        {\n" +
            "            \"fileUri\": \"" + FILE_URI + "\",\n" +
            "            \"localeId\": \"de-DE\",\n" +
            "            \"publishedDate\": \"2018-07-21T00:56:34Z\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"totalCount\": 1\n" +
            "}\n";
        assignResponse(200, String.format(SUCCESS_RESPONSE_ENVELOPE, getRecentlyPublishedFilesListResponseBody));

        ListResponse<RecentlyPublishedFileItemPTO> response = filesApi.getRecentlyPublishedFiles(
            PROJECT_ID,
            GetRecentlyPublishedFilesPTO.builder()
                .publishedAfter("2018-07-20T00:6:34Z")
                .fileUris(singletonList(FILE_URI))
                .localeIds(singletonList("de-DE"))
                .limit(10)
                .offset(100)
                .build());

        assertEquals(1, response.getItems().size());
        assertEquals(FILE_URI, response.getItems().get(0).getFileUri());
        assertEquals(date("2018-07-21T00:56:34Z"), response.getItems().get(0).getPublishedDate());
        assertEquals("de-DE", response.getItems().get(0).getLocaleId());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertTrue(recordedRequest.getPath().contains("/published-files-api/v2/projects/" + PROJECT_ID + "/files/list/recently-published"));
        assertTrue(recordedRequest.getPath().contains("fileUris" + URLEncoder.encode("[]", "UTF-8") + "=" + FILE_URI));
        assertTrue(recordedRequest.getPath().contains("publishedAfter=" + URLEncoder.encode("2018-07-20T00:6:34Z", "UTF-8")));
        assertTrue(recordedRequest.getPath().contains("localeIds" + URLEncoder.encode("[]", "UTF-8") + "=" + "de-DE"));
        assertTrue(recordedRequest.getPath().contains("limit=10"));
        assertTrue(recordedRequest.getPath().contains("offset=100"));
    }

    @Test(expected = RestApiRuntimeException.class)
    public void testHandleError() throws Exception
    {
        // language=JSON
        String getFilesListResponseBody = "" +
            "{\n" +
            "    \"items\": [\n" +
            "        {\n" +
            "            \"fileUri\": \"" + FILE_URI + "\",\n" +
            "            \"lastUploaded\": \"2018-07-21T00:56:35Z\",\n" +
            "            \"created\": \"2018-07-21T00:56:34Z\",\n" +
            "            \"fileType\": \"xml\",\n" +
            "            \"hasInstructions\": false\n" +
            "        }\n" +
            "    ],\n" +
            "    \"totalCount\": 1\n" +
            "}\n";
        assignResponse(500, String.format(SUCCESS_RESPONSE_ENVELOPE, getFilesListResponseBody));

        filesApi.getFilesList(
            PROJECT_ID,
            GetFilesListPTO.builder()
                .fileTypes(Arrays.asList(FileType.XML, FileType.JSON))
                .uriMask("uri*mask.json")
                .lastUploadedAfter("2018-01-01T00:56:34Z")
                .lastUploadedBefore("2019-01-01T00:56:34Z")
                .limit(10)
                .offset(100)
                .orderBy(OrderBy.LAST_UPLOADED_DESC)
                .build());
    }

    @Test
    public void testUploadFile() throws Exception
    {
        // language=JSON
        String getUploadFileResponseBody = "{\n" +
            "    \"overWritten\": true,\n" +
            "    \"stringCount\": 10,\n" +
            "    \"wordCount\": 3,\n" +
            "    \"message\" : \"uploaded\"\n" +
            "}";

        assignResponse(200, String.format(SUCCESS_RESPONSE_ENVELOPE, getUploadFileResponseBody));

        Map<String, String> directives = new HashMap<>();
        directives.put("dir.name.1", "dirValue1");
        directives.put("dir.name.2", "dirValue2");

        UploadFileResponse uploadFileResponse = filesApi.uploadFile(PROJECT_ID, UploadFilePTO.builder()
            .authorize(true)
            .fileType(JSON)
            .fileUri(FILE_URI)
            .file(new ByteArrayInputStream("{\"field\":\"value\"}".getBytes()))
            .directives(directives)
            .build());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
    }

    @Test
    public void testDownloadTranslatedFileMultipartSuccess() throws Exception
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

        final MockResponse response = new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MULTIPART_MIXED + "; boundary=" + boundary)
            .setBody(responseBody);
        mockWebServer.enqueue(response);

        final TranslatedFileMultipart translatedFileMultipart = filesApi.downloadTranslatedFileMultipart(
            PROJECT_ID,
            "es-ES",
            DownloadTranslationPTO.builder()
                .fileUri("myfile.properties")
                .retrievalType(RetrievalType.PENDING)
                .includeOriginalStrings(true)
                .build()
        );

        assertNotNull(translatedFileMultipart);
        assertNotNull(translatedFileMultipart.getTranslatedFileMetadata());

        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, singletonList("application/octet-stream; charset=UTF-8"));
        headers.put(HttpHeaders.CONTENT_DISPOSITION, singletonList("attachment; filename=\"myfile.properties\";"));
        assertEquals(headers, translatedFileMultipart.getFileHeaders());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM_TYPE.withCharset("UTF-8"), translatedFileMultipart.getFileMediaType());
        final InputStream inputStream = translatedFileMultipart.getFileBody();
        final String fileString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        assertEquals("key1=value1\nkey2=value2", fileString);

        final RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
    }

    @Test(expected = RestApiRuntimeException.class)
    public void testDownloadTranslatedFileMultipartWhenFailedDueToBoundaryAbsence()
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

        final MockResponse response = new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MULTIPART_MIXED)
            .setBody(responseBody);
        mockWebServer.enqueue(response);

        filesApi.downloadTranslatedFileMultipart(
            PROJECT_ID,
            "es-ES",
            DownloadTranslationPTO.builder()
                .fileUri("myfile.properties")
                .retrievalType(RetrievalType.PENDING)
                .includeOriginalStrings(true)
                .build()
        );
    }

    @Test(expected = RestApiRuntimeException.class)
    public void testDownloadTranslatedFileMultipartWhenFailedDueToBigNumberOfParts()
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
            + "--" + boundary + "\r\n"
            + "Content-Type: application/json; charset=UTF-8\r\n"
            + "\r\n"
            + "{\"key\":\"value\"}\r\n"
            + "--" + boundary + "--";

        final MockResponse response = new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MULTIPART_MIXED + "; boundary=" + boundary)
            .setBody(responseBody);
        mockWebServer.enqueue(response);

        filesApi.downloadTranslatedFileMultipart(
            PROJECT_ID,
            "es-ES",
            DownloadTranslationPTO.builder()
                .fileUri("myfile.properties")
                .retrievalType(RetrievalType.PENDING)
                .includeOriginalStrings(true)
                .build()
        );
    }

    @Test(expected = RestApiRuntimeException.class)
    public void testDownloadTranslatedFileMultipartWhenFailedDueToSmallNumberOfParts()
    {
        final String boundary = UUID.randomUUID().toString();
        final String responseBody = "--" + boundary + "\r\n"
            + "Content-Type: application/octet-stream; charset=UTF-8\r\n"
            + "Content-Disposition: attachment; filename=\"myfile.properties\";\r\n"
            + "\r\n"
            + "key1=value1\nkey2=value2\r\n"
            + "--" + boundary + "--";

        final MockResponse response = new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MULTIPART_MIXED + "; boundary=" + boundary)
            .setBody(responseBody);
        mockWebServer.enqueue(response);

        filesApi.downloadTranslatedFileMultipart(
            PROJECT_ID,
            "es-ES",
            DownloadTranslationPTO.builder()
                .fileUri("myfile.properties")
                .retrievalType(RetrievalType.PENDING)
                .includeOriginalStrings(true)
                .build()
        );
    }

    @Test(expected = RestApiRuntimeException.class)
    public void testDownloadTranslatedFileMultipartWhenFailedDueToInvalidMetadataContentType()
    {
        final String boundary = UUID.randomUUID().toString();
        final String responseBody = "--" + boundary + "\r\n"
            + "Content-Type: application/octet-stream; charset=UTF-8\r\n"
            + "Content-Disposition: attachment; filename=\"myfile.properties\";\r\n"
            + "\r\n"
            + "key1=value1\nkey2=value2\r\n"
            + "--" + boundary + "\r\n"
            + "Content-Type: application/octet-stream; charset=UTF-8\r\n"
            + "\r\n"
            + "{\"key\":\"value\"}\r\n"
            + "--" + boundary + "--";

        final MockResponse response = new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MULTIPART_MIXED + "; boundary=" + boundary)
            .setBody(responseBody);
        mockWebServer.enqueue(response);

        filesApi.downloadTranslatedFileMultipart(
            PROJECT_ID,
            "es-ES",
            DownloadTranslationPTO.builder()
                .fileUri("myfile.properties")
                .retrievalType(RetrievalType.PENDING)
                .includeOriginalStrings(true)
                .build()
        );
    }

    @Test(expected = RestApiRuntimeException.class)
    public void testDownloadTranslatedFileMultipartWhenFailedDueToAbsenceOfMetadataJson()
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
            + "\r\n"
            + "--" + boundary + "--";

        final MockResponse response = new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MULTIPART_MIXED + "; boundary=" + boundary)
            .setBody(responseBody);
        mockWebServer.enqueue(response);

        filesApi.downloadTranslatedFileMultipart(
            PROJECT_ID,
            "es-ES",
            DownloadTranslationPTO.builder()
                .fileUri("myfile.properties")
                .retrievalType(RetrievalType.PENDING)
                .includeOriginalStrings(true)
                .build()
        );
    }

    @Test(expected = RestApiRuntimeException.class)
    public void testDownloadTranslatedFileMultipartWhenFailedDueToInvalidMetadataJson()
    {
        final String boundary = UUID.randomUUID().toString();
        final String responseBody = "--" + boundary + "\r\n"
            + "Content-Type: application/octet-stream; charset=UTF-8\r\n"
            + "Content-Disposition: attachment; filename=\"myfile.properties\";\r\n"
            + "\r\n"
            + "key1=value1\nkey2=value2\r\n"
            + "--" + boundary + "\r\n"
            + "Content-Type: application/json; charset=UTF-8\r\n"
            + "{\"key\r\n"
            + "\r\n"
            + "--" + boundary + "--";

        final MockResponse response = new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MULTIPART_MIXED + "; boundary=" + boundary)
            .setBody(responseBody);
        mockWebServer.enqueue(response);

        filesApi.downloadTranslatedFileMultipart(
            PROJECT_ID,
            "es-ES",
            DownloadTranslationPTO.builder()
                .fileUri("myfile.properties")
                .retrievalType(RetrievalType.PENDING)
                .includeOriginalStrings(true)
                .build()
        );
    }

    @Test(expected = RestApiRuntimeException.class)
    public void testDownloadTranslatedFileMultipartWhenFailedDueToBrokenResponse() throws Exception
    {
        final String boundary = UUID.randomUUID().toString();
        final String responseBody = "--" + boundary + "\r\n"
            + "Content-Type: application/octet-stream; charset=UTF-8\r\n"
            + "Content-Disposition: attachment; filename=\"myfile.properties\";\r\n"
            + "\r\n"
            + "key1=value1\nkey2=";

        final MockResponse response = new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MULTIPART_MIXED + "; boundary=" + boundary)
            .setBody(responseBody);
        mockWebServer.enqueue(response);

        filesApi.downloadTranslatedFileMultipart(
            PROJECT_ID,
            "es-ES",
            DownloadTranslationPTO.builder()
                .fileUri("myfile.properties")
                .retrievalType(RetrievalType.PENDING)
                .includeOriginalStrings(true)
                .build()
        );
    }

    private static Date date(String date) throws ParseException
    {
        return FAPI_DATE_FORMAT.parse(date);
    }
}
