package com.smartling.api.accounts.v2;

import com.smartling.api.accounts.v2.pto.GetProjectsListPTO;
import com.smartling.api.accounts.v2.pto.ProjectDetailsPTO;
import com.smartling.api.v2.response.ListResponse;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/accounts-api/v2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AccountsApi
{
    /**
     * Returns the list of projects for an account.
     *
     * @param accountUid the account’s unique identifier.
     * @param getProjectsListPTO filters and pagination parameters (see {@link GetProjectsListPTO})
     * @return list of the account projects
     */
    @GET
    @Path("/accounts/{accountUid}/projects")
    ListResponse<ProjectDetailsPTO> getProjectsByAccount(
        @PathParam("accountUid") String accountUid,
        @BeanParam GetProjectsListPTO getProjectsListPTO
    );
}
