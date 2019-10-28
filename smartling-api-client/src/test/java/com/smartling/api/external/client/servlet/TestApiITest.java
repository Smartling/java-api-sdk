package com.smartling.api.external.client.servlet;

import com.smartling.api.external.client.ClientFactory;
import com.smartling.api.external.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.external.client.exception.RestApiRuntimeException;
import com.smartling.web.api.v2.ErrorIdDetails;
import com.smartling.web.api.v2.FieldErrorDetails;
import com.smartling.web.api.v2.ResponseCode;

import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestApiITest
{
    private static final String SUCCESS_RESPONSE_ENVELOPE = "{ \"response\": { \"code\": \"SUCCESS\", \"data\": %s } })";
    private static final String PACKAGE_CREATE_JSON       = "{\"packageUid\":\"b3a9764dd6d4\",\"name\":\"foo\",\"description\":null,"
            + "\"createdByUserUid\":\"4bd506227fc9\",\"createdDate\":\"2016-01-26T17:09:26Z\",\"modifiedByUserUid\":\"4bd506227fc9\",\"modifiedDate\":\"2016-01-26T17:09:26Z\","
            + "\"sourceLocaleId\":\"en-US\",\"tmUid\":\"5383bc47-972\",\"leverageUid\":\"8ed0fd02-c1e\",\"glossaries\":[{\"glossaryUid\":\"02517e79-f0a\"}],"
            + "\"targetLocaleIds\":[\"de-DE\",\"fr-FR\"],\"styleGuides\":[{\"localeId\":\"fr-FR\",\"styleGuideUid\":\"8ed0fd02\"}]}";
    private static final String PACKAGE_LIST_JSON         = "{ \"items\": [{\"packageUid\": \"uid\"}]}";
    private static final String PACKAGE_CREATE_BODY       = "{\"name\":\"foo\",\"description\":\"bar\",\"tmUid\":\"tm\",\"leverageUid\":\"leverage\","
            + "\"associateTmWithLeverage\":true,\"targetLocaleIds\":[\"de-DE\"],\"styleGuides\":[],\"glossaries\":[],\"sourceLocaleId\":\"en-US\"}";
    private static final String PACKAGE_UPDATE_BODY       = "{\"name\":\"foo\",\"description\":\"bar\",\"tmUid\":\"tm\",\"leverageUid\":\"leverage\","
            + "\"associateTmWithLeverage\":true,\"targetLocaleIds\":[\"de-DE\"],\"styleGuides\":[],\"glossaries\":[]}";
    private static final String ERROR_ERROR_ID_RESPONSE   = "{\"response\": { \"code\": \"VALIDATION_ERROR\", \"errors\": [{ \"key\": \"error_key\", \"message\": \"error "
            + "message\", \"details\":{\"errorId\":\"errorDetails\"}}] } }";
    private static final String ERROR_FIELD_RESPONSE      = "{\"response\": { \"code\": \"GENERAL_ERROR\", \"errors\": [{ \"key\": \"error_key\", \"message\": \"error message\","
            + " \"details\":{\"field\":\"field\"}}] } }";

    private static final String ACCOUNT_UID = "accountUid";
    private static final String UID         = "uid";

    private MockWebServer mockWebServer;
    private TestApi       packageApi;

    @Before
    public void setUp() throws Exception
    {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final ClientFactory factory = new ClientFactory();

        final List<ClientRequestFilter> filters = new LinkedList<>();
        filters.add(new BearerAuthStaticTokenFilter("foo"));

        packageApi = factory.build(filters, Collections.<ClientResponseFilter>emptyList(), mockWebServer.url("/").toString(), TestApi.class);
    }

    @After
    public void tearDown() throws Exception
    {
        mockWebServer.shutdown();
    }

    private void assignResponse(final int httpStatusCode, final String body)
    {
        assignResponse(httpStatusCode, body, 0);
    }

    private void assignResponse(final int httpStatusCode, final String body, final int delayMs)
    {
        final MockResponse response = new MockResponse()
                .setResponseCode(httpStatusCode)
                .setHeader(HttpHeaders.CONTENT_LENGTH, body.length())
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .setBody(body);

        if (delayMs > 0)
            response.setBodyDelay(delayMs, TimeUnit.MILLISECONDS);

        mockWebServer.enqueue(response);
    }

    private RecordedRequest getRequestWithValidation(final String httpMethod, String path) throws InterruptedException
    {
        final RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(httpMethod, request.getMethod());
        assertEquals("Bearer foo", request.getHeader(HttpHeaders.AUTHORIZATION));
        assertTrue(request.getPath().startsWith(path));
        return request;
    }

    @Test
    public void testEmptyResponse()
    {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));
        final TestDetailsPTO packageDTO = packageApi.getPackageByUid(ACCOUNT_UID, UID);
        assertNull(packageDTO);
    }

    @Test
    public void testNullData()
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, "null"));
        final TestDetailsPTO packageDTO = packageApi.getPackageByUid(ACCOUNT_UID, UID);
        assertNull(packageDTO);
    }

    @Test
    public void testEmptyData()
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, "{}"));
        final TestDetailsPTO packageDTO = packageApi.getPackageByUid(ACCOUNT_UID, UID);
        assertNotNull(packageDTO);
    }

    @Test
    public void testGetPathParameters() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, PACKAGE_CREATE_JSON));
        final TestDetailsPTO packageDTO = packageApi.getPackageByUid(ACCOUNT_UID, UID);
        assertNotNull(packageDTO);
        assertEquals("b3a9764dd6d4", packageDTO.getPackageUid());
        assertEquals("foo", packageDTO.getName());
        assertNull(packageDTO.getDescription());
        assertEquals("4bd506227fc9", packageDTO.getCreatedByUserUid());
        assertEquals("2016-01-26T17:09:26Z", packageDTO.getCreatedDate());
        assertEquals("4bd506227fc9", packageDTO.getModifiedByUserUid());
        assertEquals("2016-01-26T17:09:26Z", packageDTO.getModifiedDate());
        assertEquals(2, packageDTO.getTargetLocaleIds().size());

        assertEquals(1, packageDTO.getGlossaries().size());
        assertEquals("02517e79-f0a", packageDTO.getGlossaries().get(0).getGlossaryUid());

        assertEquals(1, packageDTO.getStyleGuides().size());
        assertEquals("fr-FR", packageDTO.getStyleGuides().get(0).getLocaleId());
        assertEquals("8ed0fd02", packageDTO.getStyleGuides().get(0).getStyleGuideUid());

        getRequestWithValidation(HttpMethod.GET, String.format("/packages-api/v2/accounts/%s/packages/%s", ACCOUNT_UID, UID));
    }

    @Test
    public void testGetQueryParameters() throws Exception
    {
        final String sourceLocaleId = "en-US";
        final String tmUid = "tm";
        final String leverageUid = "leverage";

        final List<String> targetLocaleId = new LinkedList<>();
        targetLocaleId.add("fr-FR");
        targetLocaleId.add("de-DE");

        final String styleGuideUid = "styleGuide";
        final String glossaryUid = "glossary";
        final String name = "foo";

        final TestFilterPTO filterPTO = TestFilterPTO.of(sourceLocaleId, tmUid, leverageUid, targetLocaleId, styleGuideUid, glossaryUid, name);

        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, PACKAGE_LIST_JSON));

        final TestListPTO testListPTO = packageApi.getPackageList(ACCOUNT_UID, filterPTO);
        assertNotNull(testListPTO);
        assertEquals(1, testListPTO.getItems().size());

        final TestSummaryPTO summaryPTO = testListPTO.getItems().get(0);
        assertNotNull(summaryPTO);
        assertEquals(UID, summaryPTO.getPackageUid());

        final RecordedRequest request = getRequestWithValidation(HttpMethod.GET, String.format("/packages-api/v2/accounts/%s/packages", ACCOUNT_UID));
        assertTrue(request.getPath().contains(String.format("sourceLocaleId=%s", sourceLocaleId)));
        assertTrue(request.getPath().contains(String.format("tmUid=%s", tmUid)));
        assertTrue(request.getPath().contains(String.format("leverageUid=%s", leverageUid)));
        assertTrue(request.getPath().contains(String.format("targetLocaleId=%s", targetLocaleId.get(0))));
        assertTrue(request.getPath().contains(String.format("targetLocaleId=%s", targetLocaleId.get(1))));
        assertTrue(request.getPath().contains(String.format("styleGuideUid=%s", styleGuideUid)));
        assertTrue(request.getPath().contains(String.format("glossaryUid=%s", glossaryUid)));
        assertTrue(request.getPath().contains(String.format("name=%s", name)));
    }

    @Test
    public void testPostBody() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, PACKAGE_CREATE_JSON));

        final TestCreatePTO packageCreatePTO = new TestCreatePTO();
        packageCreatePTO.setSourceLocaleId("en-US");
        packageCreatePTO.setName("foo");
        packageCreatePTO.setTmUid("tm");
        packageCreatePTO.setAssociateTmWithLeverage(true);
        packageCreatePTO.setDescription("bar");
        packageCreatePTO.setLeverageUid("leverage");

        final List<String> targetLocaleId = new LinkedList<>();
        targetLocaleId.add("de-DE");
        packageCreatePTO.setTargetLocaleIds(targetLocaleId);

        final TestDetailsPTO testDetailsPTO = packageApi.createPackage(ACCOUNT_UID, packageCreatePTO);
        assertNotNull(testDetailsPTO);

        final RecordedRequest request = getRequestWithValidation(HttpMethod.POST, String.format("/packages-api/v2/accounts/%s/packages", ACCOUNT_UID));
        final String body = new String(request.getBody().readByteArray(), "UTF-8");
        assertEquals(PACKAGE_CREATE_BODY, body);
    }

    @Test
    public void testPutBody() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, PACKAGE_CREATE_JSON));

        final TestUpdatePTO testUpdatePTO = new TestUpdatePTO();
        testUpdatePTO.setName("foo");
        testUpdatePTO.setTmUid("tm");
        testUpdatePTO.setAssociateTmWithLeverage(true);
        testUpdatePTO.setDescription("bar");
        testUpdatePTO.setLeverageUid("leverage");

        final List<String> targetLocaleId = new LinkedList<>();
        targetLocaleId.add("de-DE");
        testUpdatePTO.setTargetLocaleIds(targetLocaleId);

        final TestDetailsPTO testDetailsPTO = packageApi.updatePackage(ACCOUNT_UID, UID, testUpdatePTO);
        assertNotNull(testDetailsPTO);

        final RecordedRequest request = getRequestWithValidation(HttpMethod.PUT, String.format("/packages-api/v2/accounts/%s/packages/%s", ACCOUNT_UID, UID));
        final String body = new String(request.getBody().readByteArray(), "UTF-8");
        assertEquals(PACKAGE_UPDATE_BODY, body);
    }

    @Test
    public void testDelete() throws Exception
    {
        assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, "null"));
        packageApi.deletePackage(ACCOUNT_UID, UID);
        getRequestWithValidation(HttpMethod.DELETE, String.format("/packages-api/v2/accounts/%s/packages/%s", ACCOUNT_UID, UID));
    }

    @Test
    public void testConcurrentThreads() throws Exception
    {
        final int calls = 200;

        for (int i = 0; i < calls; i++)
            assignResponse(HttpStatus.SC_OK, String.format(SUCCESS_RESPONSE_ENVELOPE, PACKAGE_CREATE_JSON), 100);

        final Callable<TestDetailsPTO> callable = new Callable<TestDetailsPTO>() {
            @Override
            public TestDetailsPTO call() throws Exception
            {
                return packageApi.getPackageByUid(ACCOUNT_UID, UID);
            }
        };

        final ExecutorService executorService = Executors.newFixedThreadPool(calls);

        try
        {
            List<Future<TestDetailsPTO>> futures = new LinkedList<>();
            for (int i = 0; i < calls; i++)
                futures.add(executorService.submit(callable));

            for (final Future<TestDetailsPTO> future : futures)
            {
                final TestDetailsPTO testDetailsPTO = future.get();
                assertNotNull(testDetailsPTO);
                assertEquals("b3a9764dd6d4", testDetailsPTO.getPackageUid());
            }
        }
        finally
        {
            executorService.shutdown();
        }
    }

    @Test
    public void testErrorResponseWithErrorDetails() throws Exception
    {
        assignResponse(HttpStatus.SC_BAD_REQUEST, ERROR_ERROR_ID_RESPONSE);
        try
        {
            packageApi.getPackageByUid(ACCOUNT_UID, UID);
            assertFalse(true);
        }
        catch (RestApiRuntimeException ex)
        {
            assertEquals(HttpStatus.SC_BAD_REQUEST, ex.getStatus());
            assertEquals(ResponseCode.VALIDATION_ERROR, ex.getResponseCode());
            assertEquals(1, ex.getErrors().size());
            assertEquals("error_key", ex.getErrors().get(0).getKey());
            assertEquals("error message", ex.getErrors().get(0).getMessage());
            assertTrue(ex.getErrors().get(0).getDetails() instanceof ErrorIdDetails);
            assertEquals("errorDetails", ((ErrorIdDetails)ex.getErrors().get(0).getDetails()).getErrorId());
        }
    }

    @Test
    public void testErrorResponseWithFieldDetails() throws Exception
    {
        assignResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, ERROR_FIELD_RESPONSE);
        try
        {
            packageApi.getPackageByUid(ACCOUNT_UID, UID);
            assertFalse(true);
        }
        catch (RestApiRuntimeException ex)
        {
            assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, ex.getStatus());
            assertEquals(ResponseCode.GENERAL_ERROR, ex.getResponseCode());
            assertEquals(1, ex.getErrors().size());
            assertEquals("error_key", ex.getErrors().get(0).getKey());
            assertEquals("error message", ex.getErrors().get(0).getMessage());
            assertTrue(ex.getErrors().get(0).getDetails() instanceof FieldErrorDetails);
            assertEquals("field", ((FieldErrorDetails)ex.getErrors().get(0).getDetails()).getField());
        }
    }

    @Test
    public void testStreamResponse() throws Exception
    {
        final MockResponse response = new MockResponse()
                .setResponseCode(HttpStatus.SC_OK)
                .setHeader(HttpHeaders.CONTENT_TYPE, "text/csv; charset=utf-8")
                .setBody("a,b,c");

        mockWebServer.enqueue(response);

        try (InputStream stream = packageApi.exportPackage(ACCOUNT_UID, UID))
        {
            String content = IOUtils.toString(stream, "UTF-8");
            assertEquals("a,b,c", content);
        }
    }
}
