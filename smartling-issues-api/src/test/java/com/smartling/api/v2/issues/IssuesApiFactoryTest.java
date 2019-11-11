package com.smartling.api.v2.issues;

import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.v2.issues.pto.IssuePTO;
import com.smartling.api.v2.issues.pto.IssueTemplatePTO;
import com.smartling.api.v2.issues.pto.IssueTextPTO;
import com.smartling.api.v2.issues.pto.IssueTextTemplatePTO;
import com.smartling.api.v2.issues.pto.StringTemplatePTO;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;

import static org.junit.Assert.*;

public class IssuesApiFactoryTest
{
    private MockWebServer mockWebServer;
    private IssuesApi issuesApi;

    @Before
    public void setUp() throws Exception
    {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final IssuesApiFactory factory = new IssuesApiFactory();
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("foo");

        issuesApi = factory.buildApi(tokenFilter, mockWebServer.url("/").toString());
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
    public void testCreateIssue() throws Exception
    {
        String expectedResponse = "{\n" +
            "    \"response\": {\n" +
            "        \"data\": {\n" +
            "            \"issueUid\": \"uid\",\n" +
            "            \"string\": {\n" +
            "                \"hashcode\": \"hashcode\",\n" +
            "                \"localeId\": null\n" +
            "            },\n" +
            "            \"resolvedByUserUid\": \"resolvedByUserUid\",\n" +
            "            \"reportedByUserUid\": \"reportedByUserUid\",\n" +
            "            \"assigneeUserUid\": \"assigneeUserUid\",\n" +
            "            \"createdDate\": \"2015-11-21T01:51:17Z\",\n" +
            "            \"resolvedDate\": \"2015-11-21T01:51:17Z\",\n" +
            "            \"issueTextModifiedDate\": \"2015-11-21T01:51:17Z\",\n" +
            "            \"issueText\": \"text\",\n" +
            "            \"projectId\": \"projectId\",\n" +
            "            \"issueTypeCode\": \"SOURCE\",\n" +
            "            \"issueSubTypeCode\": \"CLARIFICATION\",\n" +
            "            \"issueStateCode\": \"OPENED\",\n" +
            "            \"answered\": false,\n" +
            "            \"issueSeverityLevelCode\": \"LOW\"\n" +
            "        }\n" +
            "    }\n" +
            "}";

        assignResponse(200, expectedResponse);

        IssuePTO issue = issuesApi.create("projectId", new IssueTemplatePTO(
            new StringTemplatePTO("hashcode", null),
            "SOURCE",
            "CLARIFICATION",
            "text",
            "LOW"));

        assertEquals("uid", issue.getIssueUid());
        assertEquals("hashcode", issue.getString().getHashcode());
        assertNull(issue.getString().getLocaleId());
        assertEquals("resolvedByUserUid", issue.getResolvedByUserUid());
        assertEquals("reportedByUserUid", issue.getReportedByUserUid());
        assertEquals("assigneeUserUid", issue.getAssigneeUserUid());
        assertNotNull(issue.getCreatedDate());
        assertNotNull(issue.getResolvedDate());
        assertNotNull(issue.getIssueTextModifiedDate());
        assertEquals("text", issue.getIssueText());
        assertEquals("projectId", issue.getProjectId());
        assertEquals("SOURCE", issue.getIssueTypeCode());
        assertEquals("CLARIFICATION", issue.getIssueSubTypeCode());
        assertEquals("OPENED", issue.getIssueStateCode());
        assertFalse(issue.getAnswered());
        assertEquals("LOW", issue.getIssueSeverityLevelCode());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertTrue(recordedRequest.getPath().endsWith("/issues-api/v2/projects/projectId/issues"));
    }

    @Test
    public void testUpdateIssueText() throws Exception
    {
        String expectedResponse = "{\n" +
            "    \"response\": {\n" +
            "        \"data\": {\n" +
            "            \"issueText\": \"text\",\n" +
            "            \"issueTextModifiedDate\": \"2015-11-21T01:51:17Z\"\n" +
            "        }\n" +
            "    }\n" +
            "}";

        assignResponse(200, expectedResponse);

        IssueTextPTO issueTextPTO = issuesApi.updateIssueText("projectId", "issueUid", new IssueTextTemplatePTO("text"));

        assertEquals("text", issueTextPTO.getIssueText());
        assertNotNull(issueTextPTO.getIssueTextModifiedDate());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("PUT", recordedRequest.getMethod());
        assertTrue(recordedRequest.getPath().endsWith("/issues-api/v2/projects/projectId/issues/issueUid/issueText"));
    }

    @Test
    public void testDeleteIssueAssigneeUser() throws Exception
    {
        String expectedResponse = "{\n" +
            "    \"response\": {\n" +
            "        \"data\": {}\n" +
            "    }\n" +
            "}";

        assignResponse(200, expectedResponse);

        issuesApi.deleteIssueAssigneeUser("projectId", "issueUid");

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("DELETE", recordedRequest.getMethod());
        assertTrue(recordedRequest.getPath().endsWith("/issues-api/v2/projects/projectId/issues/issueUid/assignee"));
    }
}
