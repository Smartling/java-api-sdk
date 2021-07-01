package com.smartling.api.contexts.v2;

import com.smartling.api.contexts.v2.pto.BatchBindingPTO;
import com.smartling.api.contexts.v2.pto.BatchBindingRequestPTO;
import com.smartling.api.contexts.v2.pto.BatchDeleteBindingsRequestPTO;
import com.smartling.api.contexts.v2.pto.BindingPTO;
import com.smartling.api.contexts.v2.pto.BindingsRequestPTO;
import com.smartling.api.contexts.v2.pto.ContextPTO;
import com.smartling.api.contexts.v2.pto.ContextUploadPTO;
import com.smartling.api.contexts.v2.pto.ContextUploadAndMatchPTO;
import com.smartling.api.contexts.v2.pto.MatchIdPTO;
import com.smartling.api.contexts.v2.pto.MatchRequestPTO;
import com.smartling.api.contexts.v2.pto.MatchStatusPTO;
import com.smartling.api.contexts.v2.pto.PaginatedListResponse;
import com.smartling.api.v2.response.ListResponse;
import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

@Path("/context-api/v2")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ContextsApi
{
    @POST
    @Path("/projects/{projectId}/contexts")
    @Consumes (MediaType.MULTIPART_FORM_DATA)
    ContextPTO uploadContext(@PathParam("projectId") String projectId, @MultipartForm ContextUploadPTO contextUploadPTO);

    @GET
    @Path("/projects/{projectId}/contexts")
    @Produces()
    PaginatedListResponse<ContextPTO> listContextsByProject(@PathParam("projectId") String projectId, @QueryParam("nameFilter") String nameFilter, @QueryParam("offset") String offset, @QueryParam("type") String contextType);

    @GET
    @Path("/projects/{projectId}/contexts/{contextUid}")
    ContextPTO getContext(@PathParam("projectId") String projectId, @PathParam("contextUid") String contextUid);

    @DELETE
    @Path("/projects/{projectId}/contexts/{contextUid}")
    void deleteContext(@PathParam("projectId") String projectId, @PathParam("contextUid") String contextUid);

    @GET
    @Path("/projects/{projectId}/contexts/{contextUid}/content")
    InputStream downloadContextFileContent(@PathParam("projectId") String projectId, @PathParam("contextUid") String contextUid);

    @POST
    @Path("/projects/{projectId}/contexts/{contextUid}/match/async")
    MatchIdPTO matchAsync(@PathParam("projectId") String projectId, @PathParam("contextUid") String contextUid,
        MatchRequestPTO matchRequestPTO);

    @POST
    @Path("/projects/{projectId}/contexts/upload-and-match-async")
    @Consumes (MediaType.MULTIPART_FORM_DATA)
    MatchIdPTO uploadContextAndMatchAsync(@PathParam("projectId") String projectId,
        @MultipartForm ContextUploadAndMatchPTO contextUploadPTO);

    @GET
    @Path("/projects/{projectId}/match/{matchId}")
    MatchStatusPTO matchAsyncStatus(@PathParam("projectId") String projectId, @PathParam("matchId") String matchId);

    @POST
    @Path("/projects/{projectId}/bindings")
    @Produces
    BatchBindingPTO createBindings(@PathParam("projectId") String projectId, BindingsRequestPTO bindingsRequest);

    @POST
    @Path("/projects/{projectId}/bindings/list")
    @Produces
    PaginatedListResponse<BindingPTO> listBindings(@PathParam("projectId") String projectId, BatchBindingRequestPTO bindingsRequest, @QueryParam("offset") String offset);

    @POST
    @Path("/projects/{projectId}/bindings/remove")
    @Produces
    ListResponse<BindingPTO> deleteBindings(@PathParam("projectId") String projectId, BatchDeleteBindingsRequestPTO bindingsRequest);
}
