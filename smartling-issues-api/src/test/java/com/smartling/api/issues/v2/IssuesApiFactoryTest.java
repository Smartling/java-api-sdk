package com.smartling.api.issues.v2;

import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.issues.v2.pto.IssueCommentPTO;
import com.smartling.api.issues.v2.pto.IssuePTO;
import com.smartling.api.issues.v2.pto.IssueTemplatePTO;
import com.smartling.api.issues.v2.pto.IssueTextPTO;
import com.smartling.api.issues.v2.pto.IssueTextTemplatePTO;
import com.smartling.api.issues.v2.pto.StringTemplatePTO;
import com.smartling.api.v2.response.ListResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;

public class IssuesApiFactoryTest
{
    private static final String ISSUE_COMMENTS_RESPONSE = "{\n" +
        "    \"response\": {\n" +
        "        \"code\": \"SUCCESS\",\n" +
        "        \"data\": {\n" +
        "            \"totalCount\": 5,\n" +
        "            \"items\": [\n" +
        "                {\n" +
        "                    \"issueCommentUid\": \"commentUid1\",\n" +
        "                    \"commentText\": \"first comment\",\n" +
        "                    \"createdDate\": \"2015-11-21T01:51:17Z\",\n" +
        "                    \"commentTextModifiedDate\": \"2015-11-22T02:00:00Z\",\n" +
        "                    \"createdByUserUid\": \"userUid1\",\n" +
        "                    \"createdByWatcherUid\": null,\n" +
        "                    \"taggedUsers\": []\n" +
        "                },\n" +
        "                {\n" +
        "                    \"issueCommentUid\": \"commentUid2\",\n" +
        "                    \"commentText\": \"second comment\",\n" +
        "                    \"createdDate\": \"2015-11-23T03:00:00Z\",\n" +
        "                    \"commentTextModifiedDate\": null,\n" +
        "                    \"createdByUserUid\": null,\n" +
        "                    \"createdByWatcherUid\": \"watcherUid2\",\n" +
        "                    \"taggedUsers\": []\n" +
        "                }\n" +
        "            ]\n" +
        "        }\n" +
        "    }\n" +
        "}";

    private MockWebServer mockWebServer;
    private IssuesApi issuesApi;

    @Before
    public void setUp() throws Exception
    {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        final IssuesApiFactory factory = new IssuesApiFactory();
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter("foo");

        ClientConfiguration config = DefaultClientConfiguration.builder().baseUrl(mockWebServer.url("/").url()).build();

        issuesApi = factory.buildApi(tokenFilter, config);
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

        IssuePTO issue = issuesApi.createIssue("projectId", new IssueTemplatePTO(
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

    @Test
    public void testGetIssueCommentsWithPaging() throws Exception
    {
        assignResponse(200, ISSUE_COMMENTS_RESPONSE);
        IssueCommentPTO expectedComment1 = new IssueCommentPTO(
            "commentUid1",
            "first comment",
            Date.from(Instant.parse("2015-11-21T01:51:17Z")),
            Date.from(Instant.parse("2015-11-22T02:00:00Z")),
            "userUid1",
            null);
        IssueCommentPTO expectedComment2 = new IssueCommentPTO(
            "commentUid2",
            "second comment",
            Date.from(Instant.parse("2015-11-23T03:00:00Z")),
            null,
            null,
            "watcherUid2");

        ListResponse<IssueCommentPTO> comments = issuesApi.getIssueComments("projectId", "issueUid", 2, 3);

        assertEquals(5, comments.getTotalCount());
        assertEquals(Arrays.asList(expectedComment1, expectedComment2), comments.getItems());
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/issues-api/v2/projects/projectId/issues/issueUid/comments?limit=2&offset=3", recordedRequest.getPath());
    }

    @Test
    public void testGetIssueCommentsWithNullPagingOmitsQueryParams() throws Exception
    {
        assignResponse(200, ISSUE_COMMENTS_RESPONSE);

        issuesApi.getIssueComments("projectId", "issueUid", null, null);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/issues-api/v2/projects/projectId/issues/issueUid/comments", recordedRequest.getPath());
    }

    @Test
    public void testGetIssueCommentsWithoutPaging() throws Exception
    {
        assignResponse(200, ISSUE_COMMENTS_RESPONSE);

        ListResponse<IssueCommentPTO> comments = issuesApi.getIssueComments("projectId", "issueUid");

        assertEquals(5, comments.getTotalCount());
        assertEquals(2, comments.getItems().size());
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/issues-api/v2/projects/projectId/issues/issueUid/comments", recordedRequest.getPath());
    }
}
