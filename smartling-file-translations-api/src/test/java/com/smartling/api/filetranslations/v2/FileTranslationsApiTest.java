package com.smartling.api.filetranslations.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.smartling.api.filetranslations.v2.pto.Error;
import com.smartling.api.filetranslations.v2.pto.FileType;
import com.smartling.api.filetranslations.v2.pto.LanguagePTO;
import com.smartling.api.filetranslations.v2.pto.file.FileUploadPTO;
import com.smartling.api.filetranslations.v2.pto.file.FileUploadRequest;
import com.smartling.api.filetranslations.v2.pto.file.FileUploadResponse;
import com.smartling.api.filetranslations.v2.pto.ld.LanguageDetectionResponse;
import com.smartling.api.filetranslations.v2.pto.ld.LanguageDetectionState;
import com.smartling.api.filetranslations.v2.pto.ld.LanguageDetectionStatusResponse;
import com.smartling.api.filetranslations.v2.pto.mt.MtLocaleStatus;
import com.smartling.api.filetranslations.v2.pto.mt.MtRequest;
import com.smartling.api.filetranslations.v2.pto.mt.MtResponse;
import com.smartling.api.filetranslations.v2.pto.mt.MtState;
import com.smartling.api.filetranslations.v2.pto.mt.MtStatusResponse;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.v2.response.EmptyData;
import okhttp3.Headers;
import okhttp3.MultipartReader;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.mail.internet.ContentDisposition;
import javax.mail.internet.ContentType;
import javax.mail.internet.ParseException;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FileTranslationsApiTest
{
    private static final String SUCCESS_RESPONSE_ENVELOPE = "{\"response\":{\"code\":\"SUCCESS\",\"data\":%s}})";
    private static final String ACCOUNT_UID = RandomStringUtils.randomAlphabetic(10);
    private static final String FILE_UID = RandomStringUtils.randomAlphabetic(10);
    private static final String MT_UID = RandomStringUtils.randomAlphabetic(10);
    private static final String LANGUAGE_DETECTION_UID = RandomStringUtils.randomAlphabetic(10);
    private static final String SOURCE_LOCALE_ID = RandomStringUtils.randomAlphabetic(10);
    private static final String TARGET_LOCALE_ID_1 = RandomStringUtils.randomAlphabetic(5);
    private static final String TARGET_LOCALE_ID_2 = RandomStringUtils.randomAlphabetic(5);

    private MockWebServer mockWebServer;
    private FileTranslationsApi sut;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Before
    public void setUp() throws Exception
    {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final FileTranslationsApiFactory factory = new FileTranslationsApiFactory();
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("authToken");
        final ClientConfiguration config = DefaultClientConfiguration.builder().baseUrl(mockWebServer.url("/").url()).build();

        sut = factory.buildApi(tokenFilter, config);
    }

    @After
    public void tearDown() throws Exception
    {
        sut.close();
        mockWebServer.shutdown();
    }

    @Test
    public void uploadFile() throws InterruptedException
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, String.format("{\"fileUid\":\"%s\"}", FILE_UID)));

        FileUploadRequest request = new FileUploadRequest();
        request.setFileType(FileType.PLAIN_TEXT.getIdentifier());
        FileUploadPTO fileUploadPTO = new FileUploadPTO();
        fileUploadPTO.setRequest(request);
        fileUploadPTO.setFile(new ByteArrayInputStream("whatever".getBytes(StandardCharsets.UTF_8)));


        FileUploadResponse response = sut.uploadFile(ACCOUNT_UID, fileUploadPTO);

        LinkedHashMap<String, Part> parts = toParts(getRequestWithValidation(HttpMethod.POST, String.format("/file-translations-api/v2/accounts/%s/files", ACCOUNT_UID)));
        assertThat(toObj(parts.get("request").getBodyUtf8(), FileUploadRequest.class).getFileType(), is(FileType.PLAIN_TEXT.getIdentifier()));
        assertThat(parts.get("file").getBodyUtf8(), is("whatever"));

        assertNotNull(response);
        assertEquals(FILE_UID, response.getFileUid());
    }

    @Test
    public void mtFile() throws InterruptedException
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, String.format("{\"mtUid\":\"%s\"}", MT_UID)));

        MtRequest request = new MtRequest();
        request.setSourceLocaleId(SOURCE_LOCALE_ID);
        request.setTargetLocaleIds(ImmutableList.of(TARGET_LOCALE_ID_1, TARGET_LOCALE_ID_2));

        MtResponse response = sut.mtFile(ACCOUNT_UID, FILE_UID, request);

        MtRequest recordedRequest = toObj(getRequestWithValidation(HttpMethod.POST,
            String.format("/file-translations-api/v2/accounts/%s/files/%s/mt", ACCOUNT_UID, FILE_UID)).getBody().readUtf8(), MtRequest.class);
        assertThat(recordedRequest.getSourceLocaleId(), is(SOURCE_LOCALE_ID));
        assertThat(recordedRequest.getTargetLocaleIds(), is(ImmutableList.of(TARGET_LOCALE_ID_1, TARGET_LOCALE_ID_2)));

        assertNotNull(response);
        assertEquals(MT_UID, response.getMtUid());
    }

    @Test
    public void cancelFileProcessing() throws InterruptedException, UnsupportedEncodingException
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, "{}"));

        EmptyData response = sut.cancelMt(ACCOUNT_UID, FILE_UID, MT_UID);

        getRequestWithValidation(HttpMethod.POST, String.format("/file-translations-api/v2/accounts/%s/files/%s/mt/%s/cancel", ACCOUNT_UID, FILE_UID, MT_UID));
        assertNotNull(response);
    }

    @Test
    public void getMTTranslatedFileForLocale() throws InterruptedException, IOException
    {
        final String plainTextFileContent = "Some weird strings \n inside the file";
        final MockResponse response = new MockResponse()
            .setResponseCode(HttpStatus.SC_OK)
            .setHeader(HttpHeaders.CONTENT_LENGTH, plainTextFileContent.length())
            .setHeader(HttpHeaders.CONTENT_TYPE, FileType.PLAIN_TEXT.getMimeType())
            .setBody(plainTextFileContent);
        mockWebServer.enqueue(response);

        InputStream translatedFileStream = sut.downloadTranslatedFile(ACCOUNT_UID, FILE_UID, MT_UID, TARGET_LOCALE_ID_1);

        getRequestWithValidation(HttpMethod.GET, String.format("/file-translations-api/v2/accounts/%s/files/%s/mt/%s/locales/%s/file",
            ACCOUNT_UID, FILE_UID, MT_UID, TARGET_LOCALE_ID_1));
        assertNotNull(translatedFileStream);
        assertEquals(plainTextFileContent, IOUtils.toString(translatedFileStream, StandardCharsets.UTF_8));
    }

    @Test
    public void getMTTranslatedFileZip() throws InterruptedException, IOException
    {
        final String zipArchiveContent = "Some weird strings \n inside the file";
        final MockResponse response = new MockResponse()
            .setResponseCode(HttpStatus.SC_OK)
            .setHeader(HttpHeaders.CONTENT_LENGTH, zipArchiveContent.length())
            .setHeader(HttpHeaders.CONTENT_TYPE, "application/zip")
            .setBody(zipArchiveContent);
        mockWebServer.enqueue(response);

        InputStream translatedFileStream = sut.downloadAllTranslatedFilesInZip(ACCOUNT_UID, FILE_UID, MT_UID);

        getRequestWithValidation(HttpMethod.GET,
            String.format("/file-translations-api/v2/accounts/%s/files/%s/mt/%s/locales/all/file/zip", ACCOUNT_UID, FILE_UID, MT_UID));

        assertNotNull(translatedFileStream);
        assertEquals(zipArchiveContent, IOUtils.toString(translatedFileStream, StandardCharsets.UTF_8));
    }

    @Test
    public void getMTStatus() throws InterruptedException, IOException
    {
        MtStatusResponse response = new MtStatusResponse();
        response.setState(MtState.COMPLETED);
        response.setRequestedStringCount(100L);
        Error<Map<String,String>> error = new Error<>("some.key", "some message", ImmutableMap.of("key1", "val1"));
        response.setError(error);
        MtLocaleStatus locale1Status = new MtLocaleStatus();
        locale1Status.setLocaleId(TARGET_LOCALE_ID_1);
        locale1Status.setState(MtState.PROCESSING);
        locale1Status.setProcessedStringCount(100L);

        MtLocaleStatus locale2Status = new MtLocaleStatus();
        locale2Status.setLocaleId(TARGET_LOCALE_ID_2);
        locale2Status.setState(MtState.FAILED);
        locale2Status.setProcessedStringCount(20L);
        Error<Map<String,String>> local2Error = new Error<>("some.locale2.key", "some locale 2 message", null);
        locale2Status.setError(local2Error);


        response.getLocaleProcessStatuses().add(locale1Status);
        response.getLocaleProcessStatuses().add(locale2Status);

        String responseBody = objectMapper.writeValueAsString(response);
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, responseBody));



        MtStatusResponse status = sut.getMtProgress(ACCOUNT_UID, FILE_UID, MT_UID);


        getRequestWithValidation(HttpMethod.GET,
            String.format("/file-translations-api/v2/accounts/%s/files/%s/mt/%s/status", ACCOUNT_UID, FILE_UID, MT_UID));

        assertNotNull(status);
        assertEquals(response.getState(), status.getState());
        assertEquals(response.getRequestedStringCount(), status.getRequestedStringCount());
        assertThat(response.getError().getKey(), is("some.key"));
        assertThat(response.getError().getMessage(), is("some message"));
        assertThat(response.getError().getDetails(), is(ImmutableMap.of("key1", "val1")));
        assertEquals(response.getLocaleProcessStatuses().size(), status.getLocaleProcessStatuses().size());
        assertEquals(response.getLocaleProcessStatuses().get(0).getLocaleId(), status.getLocaleProcessStatuses().get(0).getLocaleId());
        assertEquals(response.getLocaleProcessStatuses().get(0).getProcessedStringCount(), status.getLocaleProcessStatuses().get(0).getProcessedStringCount());
        assertEquals(response.getLocaleProcessStatuses().get(0).getState(), status.getLocaleProcessStatuses().get(0).getState());
        assertThat(response.getLocaleProcessStatuses().get(0).getError(), nullValue());

        assertEquals(response.getLocaleProcessStatuses().get(1).getLocaleId(), status.getLocaleProcessStatuses().get(1).getLocaleId());
        assertEquals(response.getLocaleProcessStatuses().get(1).getProcessedStringCount(), status.getLocaleProcessStatuses().get(1).getProcessedStringCount());
        assertEquals(response.getLocaleProcessStatuses().get(1).getState(), status.getLocaleProcessStatuses().get(1).getState());
        assertThat(response.getLocaleProcessStatuses().get(1).getError().getKey(), is("some.locale2.key"));
        assertThat(response.getLocaleProcessStatuses().get(1).getError().getMessage(), is("some locale 2 message"));
        assertThat(response.getLocaleProcessStatuses().get(1).getError().getDetails(), nullValue());
    }

    @Test
    public void detectSourceLanguage() throws InterruptedException
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, String.format(
            "{\"languageDetectionUid\":\"%s\"}", LANGUAGE_DETECTION_UID)));

        LanguageDetectionResponse response = sut.detectFileSourceLanguage(ACCOUNT_UID, FILE_UID);

        getRequestWithValidation(HttpMethod.POST, String.format("/file-translations-api/v2/accounts/%s/files/%s/language-detection", ACCOUNT_UID, FILE_UID));
        assertNotNull(response);
        assertEquals(LANGUAGE_DETECTION_UID, response.getLanguageDetectionUid());
    }

    @Test
    public void getLDStatus() throws InterruptedException, IOException
    {
        LanguageDetectionStatusResponse response = new LanguageDetectionStatusResponse();
        Error<Map<String,String>> error = new Error<>("some.key", "some message", ImmutableMap.of("key1", "val1"));
        response.setError(error);
        response.setState(LanguageDetectionState.COMPLETED);
        LanguagePTO languagePTO1 = new LanguagePTO();
        languagePTO1.setLanguageId("fr");
        languagePTO1.setDefaultLocaleId("fr-FR");
        LanguagePTO languagePTO2 = new LanguagePTO();
        languagePTO2.setLanguageId("de");
        languagePTO2.setDefaultLocaleId("de-DE");
        response.setDetectedSourceLanguages(ImmutableList.of(languagePTO1, languagePTO2));
        String responseBody = objectMapper.writeValueAsString(response);
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, responseBody));

        LanguageDetectionStatusResponse status = sut.getLanguageDetectionStatus(ACCOUNT_UID, FILE_UID, LANGUAGE_DETECTION_UID);

        getRequestWithValidation(HttpMethod.GET,
            String.format("/file-translations-api/v2/accounts/%s/files/%s/language-detection/%s/status", ACCOUNT_UID, FILE_UID, LANGUAGE_DETECTION_UID));
        assertNotNull(status);
        assertEquals(response.getState(), status.getState());
        assertThat(response.getError().getKey(), is("some.key"));
        assertThat(response.getError().getMessage(), is("some message"));
        assertThat(response.getError().getDetails(), is(ImmutableMap.of("key1", "val1")));

        assertEquals(response.getDetectedSourceLanguages().size(), status.getDetectedSourceLanguages().size());

        assertEquals(response.getDetectedSourceLanguages().get(0).getLanguageId(), status.getDetectedSourceLanguages().get(0).getLanguageId());
        assertEquals(response.getDetectedSourceLanguages().get(0).getDefaultLocaleId(), status.getDetectedSourceLanguages().get(0).getDefaultLocaleId());
        assertEquals(response.getDetectedSourceLanguages().get(1).getLanguageId(), status.getDetectedSourceLanguages().get(1).getLanguageId());
        assertEquals(response.getDetectedSourceLanguages().get(1).getDefaultLocaleId(), status.getDetectedSourceLanguages().get(1).getDefaultLocaleId());
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

    protected RecordedRequest getRequestWithValidation(final String httpMethod, final String path) throws InterruptedException
    {
        final RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(httpMethod, request.getMethod());
        assertEquals("Bearer authToken", request.getHeader(HttpHeaders.AUTHORIZATION));
        assertEquals(request.getPath(), path);
        assertThat(request.getHeader(HttpHeaders.USER_AGENT), startsWith("smartling-file-translations-api-java/"));
        return request;
    }

    private LinkedHashMap<String, Part> toParts(RecordedRequest recordedRequest)
    {
        LinkedHashMap<String, Part> parts = new LinkedHashMap<>();

        try
        {
            MultipartReader multipartReader = new MultipartReader(recordedRequest.getBody(),
                new ContentType(recordedRequest.getHeader(HttpHeaders.CONTENT_TYPE)).getParameter("boundary"));

            MultipartReader.Part part;
            while ((part = multipartReader.nextPart()) != null)
            {
                ContentDisposition contentDisposition = new ContentDisposition(part.headers().get(HttpHeaders.CONTENT_DISPOSITION));
                parts.put(contentDisposition.getParameter("name"), new Part(part.headers(), part.body().readByteArray()));
            }

            return parts;
        } catch (ParseException|IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    private static class Part
    {
        private okhttp3.Headers headers;
        private byte[] body;

        public Part(Headers headers, byte[] body)
        {
            this.headers = headers;
            this.body = body;
        }

        public String getBodyUtf8()
        {
            return new String(body, StandardCharsets.UTF_8);
        }
    }

    private <T> T toObj(String json, Class<T> valueType)
    {
        try
        {
            return objectMapper.readValue(json, valueType);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

}
