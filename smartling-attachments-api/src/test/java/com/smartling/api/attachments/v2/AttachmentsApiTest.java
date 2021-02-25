package com.smartling.api.attachments.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartling.api.attachments.v2.pto.AttachmentEntitiesPTO;
import com.smartling.api.attachments.v2.pto.AttachmentListFilter;
import com.smartling.api.attachments.v2.pto.AttachmentPTO;
import com.smartling.api.attachments.v2.pto.AttachmentType;
import com.smartling.api.attachments.v2.pto.EntityAttachmentsPTO;
import com.smartling.api.attachments.v2.pto.EntityLinkCommand;
import com.smartling.api.attachments.v2.pto.EntityListCommand;
import com.smartling.api.attachments.v2.pto.EntityPTO;
import com.smartling.api.attachments.v2.pto.LinkedAttachmentPTO;
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
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

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
    private static final String ENTITY_ATTACHMENT = ENTITY_LINKS + "/attachment/{" + ATTACHMENT_UID_PARAMETER + "}";
    private static final String ATTACHMENTS = IDENTITY_URL + "/attachments";
    private static final String BEARER_TOKEN = UUID.randomUUID().toString();
    private static final String ACCOUNT_UID = "account_uid";
    private static final String ENTITY_UID = "entity_uid";
    private static final String ATTACHMENT_UID = "attachment_uid";
    private static final String ATTACHMENT_TYPE = AttachmentType.JOBS.name().toLowerCase();
    private static final List<String> PATH_PLACEHOLDERS = Arrays.asList(ACCOUNT_UID_PARAMETER, TYPE_PARAMETER, ENTITY_UID_PARAMETER, ATTACHMENT_UID_PARAMETER);

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

    private EntityPTO createEntityPTO()
    {
        EntityPTO entityPTO = new EntityPTO();
        entityPTO.setEntityUid(UUID.randomUUID().toString());
        entityPTO.setCreatedByUserUid(UUID.randomUUID().toString());
        return entityPTO;
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

    private LinkedAttachmentPTO createLinkedAttachmentPTO()
    {
        LinkedAttachmentPTO linkedAttachmentPTO = new LinkedAttachmentPTO();
        randomAttachmentPTO(linkedAttachmentPTO);
        return linkedAttachmentPTO;
    }

    private AttachmentEntitiesPTO createAttachmentEntitiesPTO()
    {
        AttachmentEntitiesPTO attachmentEntitiesPTO = new AttachmentEntitiesPTO();
        randomAttachmentPTO(attachmentEntitiesPTO);
        attachmentEntitiesPTO.setEntities(Arrays.asList(createEntityPTO(), createEntityPTO()));
        return attachmentEntitiesPTO;
    }

    private EntityAttachmentsPTO createEntityAttachmentsPTO()
    {
        EntityAttachmentsPTO entityAttachmentsPTO = new EntityAttachmentsPTO();
        entityAttachmentsPTO.setEntityUid(UUID.randomUUID().toString());
        entityAttachmentsPTO.setAttachments(Arrays.asList(createLinkedAttachmentPTO(), createLinkedAttachmentPTO()));
        return entityAttachmentsPTO;
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

    private void assignResponse(int httpStatusCode, String body)
    {
        MockResponse response = new MockResponse()
                .setResponseCode(httpStatusCode)
                .setHeader(HttpHeaders.CONTENT_LENGTH, body.length())
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .setBody(body);

        mockWebServer.enqueue(response);
    }

    @Test
    public void testGetApiClass() throws Exception
    {
        assertEquals(AttachmentsApi.class, new AttachmentsApiFactory().getApiClass());
    }

    @Test
    public void testListAttachments() throws Exception
    {
        AttachmentEntitiesPTO expectedPTO1 = createAttachmentEntitiesPTO();
        AttachmentEntitiesPTO expectedPTO2 = createAttachmentEntitiesPTO();

        assignResponse(HttpStatus.SC_OK, MediaType.APPLICATION_JSON, jsonResponse(new ListResponse<>(Arrays.asList(expectedPTO1, expectedPTO2))));

        AttachmentListFilter attachmentListFilter = new AttachmentListFilter();
        attachmentListFilter.setLimit(10);
        attachmentListFilter.setOffset(5);

        ListResponse<AttachmentEntitiesPTO> response = attachmentsApi.listAttachments(ACCOUNT_UID, ATTACHMENT_TYPE, attachmentListFilter);
        RecordedRequest request = getRequestWithValidation(HttpMethod.GET, ATTACHMENTS, ACCOUNT_UID, ATTACHMENT_TYPE);
        assertTrue(request.getPath().contains("offset=" + attachmentListFilter.getOffset()));
        assertTrue(request.getPath().contains("limit=" + attachmentListFilter.getLimit()));
        assertEquals(2, response.getItems().size());

        AttachmentEntitiesPTO actualPTO1 = response.getItems().get(0);
        AttachmentEntitiesPTO actualPTO2 = response.getItems().get(1);
        assertEquals(expectedPTO1.getAttachmentUid(), actualPTO1.getAttachmentUid());
        assertEquals(expectedPTO2.getAttachmentUid(), actualPTO2.getAttachmentUid());
    }

    @Test
    public void testLinkAttachments() throws Exception
    {
        EntityAttachmentsPTO expectedPTO1 = createEntityAttachmentsPTO();
        EntityAttachmentsPTO expectedPTO2 = createEntityAttachmentsPTO();

        assignResponse(HttpStatus.SC_OK, MediaType.APPLICATION_JSON, jsonResponse(new ListResponse<>(Arrays.asList(expectedPTO1, expectedPTO2))));

        EntityLinkCommand command = new EntityLinkCommand();
        command.setEntityUids(Arrays.asList("e1", "e2"));
        command.setAttachmentUids(Arrays.asList("a1", "a2"));

        ListResponse<EntityAttachmentsPTO> response = attachmentsApi.linkAttachments(ACCOUNT_UID, ATTACHMENT_TYPE, command);
        RecordedRequest request = getRequestWithValidation(HttpMethod.PUT, LINKS, ACCOUNT_UID, ATTACHMENT_TYPE);
        assertEquals(2, response.getItems().size());
        assertEquals(json(command), request.getBody().readUtf8());

        EntityAttachmentsPTO actualPTO1 = response.getItems().get(0);
        EntityAttachmentsPTO actualPTO2 = response.getItems().get(1);
        assertEquals(expectedPTO1.getEntityUid(), actualPTO1.getEntityUid());
        assertEquals(expectedPTO2.getEntityUid(), actualPTO2.getEntityUid());
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
    public void testBulkEntityAttachments() throws Exception
    {
        EntityAttachmentsPTO expectedPTO1 = createEntityAttachmentsPTO();
        EntityAttachmentsPTO expectedPTO2 = createEntityAttachmentsPTO();

        assignResponse(HttpStatus.SC_OK, MediaType.APPLICATION_JSON, jsonResponse(new ListResponse<>(Arrays.asList(expectedPTO1, expectedPTO2))));

        EntityListCommand command = new EntityListCommand();
        command.setEntityUids(Arrays.asList("e1", "e2"));

        ListResponse<EntityAttachmentsPTO> response = attachmentsApi.bulkEntityAttachments(ACCOUNT_UID, ATTACHMENT_TYPE, command);
        RecordedRequest request = getRequestWithValidation(HttpMethod.POST, LINKS, ACCOUNT_UID, ATTACHMENT_TYPE);
        assertEquals(2, response.getItems().size());
        assertEquals(json(command), request.getBody().readUtf8());

        EntityAttachmentsPTO actualPTO1 = response.getItems().get(0);
        EntityAttachmentsPTO actualPTO2 = response.getItems().get(1);
        assertEquals(expectedPTO1.getEntityUid(), actualPTO1.getEntityUid());
        assertEquals(expectedPTO2.getEntityUid(), actualPTO2.getEntityUid());
    }

    @Test
    public void testDeleteAttachments() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, MediaType.APPLICATION_JSON, jsonResponse(null));

        assertNotNull(attachmentsApi.deleteAttachments(ACCOUNT_UID, ATTACHMENT_TYPE, ENTITY_UID));
        getRequestWithValidation(HttpMethod.DELETE, ENTITY_LINKS, ACCOUNT_UID, ATTACHMENT_TYPE, ENTITY_UID);
    }

    @Test
    public void testDeleteAttachment() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, MediaType.APPLICATION_JSON, jsonResponse(null));

        assertNotNull(attachmentsApi.deleteAttachment(ACCOUNT_UID, ATTACHMENT_TYPE, ENTITY_UID, ATTACHMENT_UID));
        getRequestWithValidation(HttpMethod.DELETE, ENTITY_ATTACHMENT, ACCOUNT_UID, ATTACHMENT_TYPE, ENTITY_UID, ATTACHMENT_UID);
    }
}
