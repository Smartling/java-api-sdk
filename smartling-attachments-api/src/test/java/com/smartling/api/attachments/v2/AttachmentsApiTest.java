package com.smartling.api.attachments.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartling.api.attachments.v2.pto.AttachmentPTO;
import com.smartling.api.attachments.v2.pto.AttachmentType;
import com.smartling.api.attachments.v2.pto.AttachmentUploadPTO;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.v2.response.ListResponse;
import com.smartling.api.v2.response.ResponseData;
import com.smartling.api.v2.response.RestApiResponse;
import com.smartling.api.v2.response.SuccessResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AttachmentsApiTest
{
    private static final String ACCOUNT_UID_PARAMETER = "accountUid";
    private static final String TYPE_PARAMETER = "type";
    private static final String ENTITY_UID_PARAMETER = "entityUid";
    private static final String ATTACHMENT_UID_PARAMETER = "attachmentUid";
    private static final String API_VERSION = "/v2";
    private static final String API_ROOT = "/attachments-api" + API_VERSION;
    private static final String IDENTITY_URL = "/accounts" + "/{" + ACCOUNT_UID_PARAMETER + "}" + "/{" + TYPE_PARAMETER + "}";
    private static final String LINKS = IDENTITY_URL;
    private static final String ENTITY_LINKS = LINKS + "/{" + ENTITY_UID_PARAMETER + "}";
    private static final String ATTACHMENTS = IDENTITY_URL + "/attachments";
    private static final String DOWNLOAD_ATTACHMENT = ATTACHMENTS + "/{" + ATTACHMENT_UID_PARAMETER + "}";
    private static final String BEARER_TOKEN = UUID.randomUUID().toString();
    private static final String ACCOUNT_UID = "account_uid";
    private static final String ENTITY_UID = "entity_uid";
    private static final String ATTACHMENT_UID = "attachment_uid";
    private static final String ATTACHMENT_TYPE = AttachmentType.JOBS.name().toLowerCase();
    private static final List<String> PATH_PLACEHOLDERS = asList(ACCOUNT_UID_PARAMETER, TYPE_PARAMETER, ENTITY_UID_PARAMETER, ATTACHMENT_UID_PARAMETER);

    private MockWebServer mockWebServer;
    private AttachmentsApi attachmentsApi;

    private void assignResponse(int httpStatusCode, String contentType, String contentBody)
    {
        MockResponse response = new MockResponse()
                .setResponseCode(httpStatusCode)
                .setHeader(HttpHeaders.CONTENT_LENGTH, contentBody.length())
                .setHeader(HttpHeaders.CONTENT_TYPE, contentType)
                .setBody(contentBody);

        mockWebServer.enqueue(response);
    }

    private String requestPath(String path, String... pathParameters)
    {
        assertTrue(pathParameters.length <= PATH_PLACEHOLDERS.size());
        for (int i = 0; i < pathParameters.length; i++)
            path = path.replace("{" + PATH_PLACEHOLDERS.get(i) + "}", pathParameters[i]);

        return API_ROOT + path;
    }

    private RecordedRequest getRequestWithValidation(String httpMethod, String path, String... pathParameters) throws InterruptedException
    {
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(httpMethod, request.getMethod());
        assertEquals("Bearer " + BEARER_TOKEN, request.getHeader(HttpHeaders.AUTHORIZATION));
        assertTrue(request.getPath().startsWith(requestPath(path, pathParameters)));
        return request;
    }

    private static String json(Object command) throws JsonProcessingException
    {
        return new ObjectMapper().writeValueAsString(command);
    }

    private static <T extends ResponseData> String jsonResponse(T responseData) throws JsonProcessingException
    {
        return json(new RestApiResponse<>(new SuccessResponse<>(responseData)));
    }

    private void randomAttachmentPTO(AttachmentPTO attachmentPTO)
    {
        attachmentPTO.setAttachmentUid(UUID.randomUUID().toString());
        attachmentPTO.setName(UUID.randomUUID().toString());
        attachmentPTO.setDescription(UUID.randomUUID().toString());
        attachmentPTO.setCreatedDate(new Date());
        attachmentPTO.setCreatedByUserUid(UUID.randomUUID().toString());
    }

    private AttachmentPTO createAttachmentPTO()
    {
        AttachmentPTO attachmentPTO = new AttachmentPTO();
        randomAttachmentPTO(attachmentPTO);
        return attachmentPTO;
    }

    @Before
    public void setUp() throws Exception
    {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        ClientConfiguration config = DefaultClientConfiguration.builder().baseUrl(mockWebServer.url("/").url()).build();
        attachmentsApi = new AttachmentsApiFactory().buildApi(new BearerAuthStaticTokenFilter(BEARER_TOKEN), config);
    }

    @After
    public void tearDown() throws Exception
    {
        mockWebServer.shutdown();
    }

    @Test
    public void testGetApiClass() throws Exception
    {
        assertEquals(AttachmentsApi.class, new AttachmentsApiFactory().getApiClass());
    }

    @Test
    public void testEntityAttachments() throws Exception
    {
        AttachmentPTO expectedPTO1 = createAttachmentPTO();
        AttachmentPTO expectedPTO2 = createAttachmentPTO();

        assignResponse(HttpStatus.SC_OK, MediaType.APPLICATION_JSON, jsonResponse(new ListResponse<>(Arrays.asList(expectedPTO1, expectedPTO2))));

        ListResponse<AttachmentPTO> response = attachmentsApi.entityAttachments(ACCOUNT_UID, ATTACHMENT_TYPE, ENTITY_UID);
        getRequestWithValidation(HttpMethod.GET, ENTITY_LINKS, ACCOUNT_UID, ATTACHMENT_TYPE, ENTITY_UID);
        assertEquals(2, response.getItems().size());

        AttachmentPTO actualPTO1 = response.getItems().get(0);
        AttachmentPTO actualPTO2 = response.getItems().get(1);
        assertEquals(expectedPTO1.getAttachmentUid(), actualPTO1.getAttachmentUid());
        assertEquals(expectedPTO2.getAttachmentUid(), actualPTO2.getAttachmentUid());
    }

    @Test
    public void testUploadAttachment() throws Exception
    {
        // given
        AttachmentPTO expectedPTO = createAttachmentPTO();
        assignResponse(HttpStatus.SC_OK, MediaType.APPLICATION_JSON, jsonResponse(expectedPTO));

        AttachmentUploadPTO attachment = new AttachmentUploadPTO();
        attachment.setFile(new ByteArrayInputStream("content".getBytes()));
        attachment.setName("test.txt");
        attachment.setDescription("description");
        attachment.setEntityUids(asList("jobUuid1", "jobUuid2"));

        // when
        AttachmentPTO response = attachmentsApi.uploadAttachment(ACCOUNT_UID, ATTACHMENT_TYPE, attachment);

        // then
        RecordedRequest request = getRequestWithValidation(HttpMethod.POST, ATTACHMENTS, ACCOUNT_UID, ATTACHMENT_TYPE);
        String requestBody = request.getBody().readUtf8();
        String partSeparator = requestBody.substring(0, 38);

        assertEquals(requestBody, partSeparator + "\r\n" +
            "Content-Disposition: form-data; name=\"file\"; filename=\"file\"\r\n" +
            "Content-Type: application/octet-stream\r\n" +
            "\r\n" +
            "content\r\n" +
            partSeparator + "\r\n" +
            "Content-Disposition: form-data; name=\"name\"\r\n" +
            "Content-Type: text/plain\r\n" +
            "\r\n" +
            "test.txt\r\n" +
            partSeparator + "\r\n" +
            "Content-Disposition: form-data; name=\"description\"\r\n" +
            "Content-Type: text/plain\r\n" +
            "\r\n" +
            "description\r\n" +
            partSeparator + "\r\n" +
            "Content-Disposition: form-data; name=\"entityUids\"\r\n" +
            "Content-Type: text/plain\r\n" +
            "\r\n" +
            "jobUuid1\r\n" +
            partSeparator + "\r\n" +
            "Content-Disposition: form-data; name=\"entityUids\"\r\n" +
            "Content-Type: text/plain\r\n" +
            "\r\n" +
            "jobUuid2\r\n" +
            partSeparator + "--");

        assertEquals(expectedPTO.getAttachmentUid(), response.getAttachmentUid());
        assertEquals(expectedPTO.getName(), response.getName());
        assertEquals(expectedPTO.getDescription(), response.getDescription());
        assertEquals(expectedPTO.getCreatedDate(), response.getCreatedDate());
        assertEquals(expectedPTO.getCreatedByUserUid(), response.getCreatedByUserUid());
    }

    @Test
    public void testDownloadAttachment() throws IOException, InterruptedException {
        // given
        String attachmentContent = "Attachment content\nThe end.";
        assignResponse(HttpStatus.SC_OK, "application/zip;charset=UTF-8", attachmentContent);

        // when
        InputStream response = attachmentsApi.downloadAttachment(ACCOUNT_UID, ATTACHMENT_TYPE, ATTACHMENT_UID);

        // then

        getRequestWithValidation(HttpMethod.GET, DOWNLOAD_ATTACHMENT, ACCOUNT_UID, ATTACHMENT_TYPE, ENTITY_UID, ATTACHMENT_UID);

        assertNotNull(response);
        assertEquals(attachmentContent, IOUtils.toString(response, StandardCharsets.UTF_8));
    }
}
