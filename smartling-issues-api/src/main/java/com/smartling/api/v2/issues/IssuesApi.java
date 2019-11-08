package com.smartling.api.v2.issues;

import com.smartling.api.v2.issues.pto.CountPTO;
import com.smartling.api.v2.issues.pto.IssueAnsweredPTO;
import com.smartling.api.v2.issues.pto.IssueAssignedUserPTO;
import com.smartling.api.v2.issues.pto.IssueCommentPTO;
import com.smartling.api.v2.issues.pto.IssueCommentTemplatePTO;
import com.smartling.api.v2.issues.pto.IssuePTO;
import com.smartling.api.v2.issues.pto.IssueSeverityLevelPTO;
import com.smartling.api.v2.issues.pto.IssueStatePTO;
import com.smartling.api.v2.issues.pto.IssueTemplatePTO;
import com.smartling.api.v2.issues.pto.IssueTextPTO;
import com.smartling.api.v2.issues.pto.IssueTextTemplatePTO;
import com.smartling.api.v2.issues.pto.IssueTypePTO;
import com.smartling.api.v2.issues.pto.IssuesFilterPTO;
import com.smartling.api.v2.issues.pto.IssuesPagingFilterPTO;
import com.smartling.api.v2.issues.pto.WatcherPTO;
import com.smartling.api.v2.issues.pto.WatcherTemplatePTO;
import com.smartling.api.v2.response.EmptyData;
import com.smartling.api.v2.response.ListResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/issues-api/v2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IssuesApi
{
    @GET
    @Path("/dictionary/issue-states")
    ListResponse<IssueStatePTO> getIssueStates();

    @GET
    @Path("/accounts/{accountUid}/issue-types")
    ListResponse<IssueTypePTO> getAccountIssueTypes(@PathParam("accountUid") String accountUid);

    @POST
    @Path("/projects/{projectId}/issues")
    IssuePTO create(@PathParam("projectId") String projectId, IssueTemplatePTO issueTemplate);

    @PUT
    @Path("/projects/{projectId}/issues/{issueUid}/issueText")
    IssueTextPTO updateIssueText(@PathParam("projectId") String projectId,
                                 @PathParam("issueUid") String issueUid,
                                 IssueTextTemplatePTO issueTextTemplatePTO);

    @PUT
    @Path("/projects/{projectId}/issues/{issueUid}/state")
    IssueStatePTO updateIssueState(@PathParam("projectId") String projectId,
                                   @PathParam("issueUid") String issueUid,
                                   IssueStatePTO issueStatePTO);

    @GET
    @Path("/projects/{projectId}/issues/{issueUid}")
    IssuePTO get(@PathParam("projectId") String projectId, @PathParam("issueUid") String issueUid);

    @PUT
    @Path("/projects/{projectId}/issues/{issueUid}/answered")
    IssueAnsweredPTO updateIssueAnswered(@PathParam("projectId") String projectId,
                                         @PathParam("issueUid") String issueUid,
                                         IssueAnsweredPTO issueAnsweredPTO);

    @PUT
    @Path("/projects/{projectId}/issues/{issueUid}/assignee")
    IssueAssignedUserPTO updateIssueAssigneeUser(@PathParam("projectId") String projectId,
                                                 @PathParam("issueUid") String issueUid,
                                                 IssueAssignedUserPTO issueAnsweredPTO);

    @DELETE
    @Path("/projects/{projectId}/issues/{issueUid}/assignee")
    void deleteIssueAssigneeUser(@PathParam("projectId") String projectId,
                                      @PathParam("issueUid") String issueUid);

    @PUT
    @Path("/projects/{projectId}/issues/{issueUid}/severity-level")
    IssueSeverityLevelPTO updateIssueSeverityLevel(@PathParam("projectId") String projectId,
                                                   @PathParam("issueUid") String issueUid,
                                                   IssueSeverityLevelPTO issueAnsweredPTO);

    @POST
    @Path("/projects/{projectId}/issues/{issueUid}/comments")
    IssueCommentPTO createIssueComment(@PathParam("projectId") String projectId,
                                       @PathParam("issueUid") String issueUid,
                                       IssueCommentTemplatePTO issueCommentTemplatePTO);

    @GET
    @Path("/projects/{projectId}/issues/{issueUid}/comments")
    ListResponse<IssueCommentPTO> getIssueComments(@PathParam("projectId") String projectId,
                                                   @PathParam("issueUid") String issueUid);

    @PUT
    @Path("/projects/{projectId}/issues/{issueUid}/comments/{commentUid}")
    IssueCommentPTO updateIssueComment(@PathParam("projectId") String projectId,
                                       @PathParam("issueUid") String issueUid,
                                       @PathParam("commentUid") String commentUid,
                                       IssueCommentTemplatePTO issueCommentTemplatePTO);

    @GET
    @Path("/projects/{projectId}/issues/{issueUid}/comments/{commentUid}")
    IssueCommentPTO getIssueComment(@PathParam("projectId") String projectId,
                                    @PathParam("issueUid") String issueUid,
                                    @PathParam("commentUid") String commentUid);

    @DELETE
    @Path("/projects/{projectId}/issues/{issueUid}/comments/{commentUid}")
    void deleteIssueComment(@PathParam("projectId") String projectId,
                                 @PathParam("issueUid") String issueUid,
                                 @PathParam("commentUid") String commentUid);

    @POST
    @Path("/projects/{projectId}/issues/list")
    ListResponse<IssuePTO> getIssues(@PathParam("projectId") String projectId, IssuesPagingFilterPTO filter);

    @POST
    @Path("/projects/{projectId}/issues/count")
    CountPTO getIssuesCount(@PathParam("projectId") String projectId, IssuesFilterPTO issuesFilterPTO);

    @GET
    @Path("/accounts/{accountUid}/watchers")
    ListResponse<WatcherPTO> getAccountWatchers(@PathParam("accountUid") String accountUid,
                                                @QueryParam("email") String email,
                                                @QueryParam("limit") String limit,
                                                @QueryParam("offset") String offset);

    @POST
    @Path("/accounts/{accountUid}/watchers")
    WatcherPTO createAccountWatcher(@PathParam("accountUid") String accountUid,
                                    WatcherTemplatePTO watcherTemplatePTO);

    @PUT
    @Path("/accounts/{accountUid}/watchers/{watcherUid}")
    WatcherPTO updateAccountWatcher(@PathParam("accountUid") String accountUid,
                                    @PathParam("watcherUid") String watcherUid,
                                    WatcherTemplatePTO watcherTemplatePTO);

    @PUT
    @Path("/accounts/{accountUid}/projects/{projectId}/issues/{issueUid}/watchers/{watcherUid}")
    void addIssueWatcher(@PathParam("accountUid") String accountUid,
                         @PathParam("projectId") String projectId,
                         @PathParam("issueUid") String issueUid,
                         @PathParam("watcherUid") String watcherUid);

    @DELETE
    @Path("/accounts/{accountUid}/projects/{projectId}/issues/{issueUid}/watchers/{watcherUid}")
    void removeIssueWatcher(@PathParam("accountUid") String accountUid,
                            @PathParam("projectId") String projectId,
                            @PathParam("issueUid") String issueUid,
                            @PathParam("watcherUid") String watcherUid);

    @GET
    @Path("/accounts/{accountUid}/projects/{projectId}/issues/{issueUid}/watchers")
    ListResponse<WatcherPTO> getIssueWatchers(@PathParam("accountUid") String accountUid,
                                              @PathParam("projectId") String projectId,
                                              @PathParam("issueUid") String issueUid);

}
