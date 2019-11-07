package com.smartling.api.v2.authorization;

import com.smartling.api.v2.authorization.pto.UserIdentityDataPTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/authorization-api/v2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AuthorizationApi
{
    /**
     * Returns user authorizations for the given account UID.
     *
     * @param accountUid the account UID
     * @return a {@link UserIdentityDataPTO} containing user authorization information
     * for the given <code>accountUid</code>
     */
    @GET
    @Path("/user-details/accounts/{accountUid}")
    UserIdentityDataPTO getUserIdentityByAccount(@PathParam("accountUid") String accountUid);

    /**
     * Returns user authorizations for the given project ID.
     *
     * @param projectId the project ID
     * @return a {@link UserIdentityDataPTO} containing user authorization information for
     * the given <code>projectId</code>
     */
    @GET
    @Path("/user-details/projects/{projectId}")
    UserIdentityDataPTO getUserIdentity(@PathParam("projectId") String projectId);
}
