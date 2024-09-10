package com.smartling.api.contexts.v2;

import com.smartling.api.contexts.v2.pto.AsyncProcessPTO;
import com.smartling.api.contexts.v2.pto.AsyncProcessStartedPTO;
import com.smartling.api.contexts.v2.pto.BatchBindingPTO;
import com.smartling.api.contexts.v2.pto.BatchBindingRequestPTO;
import com.smartling.api.contexts.v2.pto.BatchDeleteBindingsRequestPTO;
import com.smartling.api.contexts.v2.pto.BindingPTO;
import com.smartling.api.contexts.v2.pto.BindingsRequestPTO;
import com.smartling.api.contexts.v2.pto.ContextPTO;
import com.smartling.api.contexts.v2.pto.ContextUploadPTO;
import com.smartling.api.contexts.v2.pto.ContextUploadAndMatchPTO;
import com.smartling.api.contexts.v2.pto.DeleteContextsAsyncRequestPTO;
import com.smartling.api.contexts.v2.pto.MatchRequestPTO;
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
public interface ContextsApi extends AutoCloseable
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

    @POST
    @Path("/projects/{projectId}/contexts/remove/async")
    AsyncProcessStartedPTO deleteContextsAsync(@PathParam("projectId") String projectId, DeleteContextsAsyncRequestPTO request);

    @GET
    @Path("/projects/{projectId}/contexts/{contextUid}/content")
    InputStream downloadContextFileContent(@PathParam("projectId") String projectId, @PathParam("contextUid") String contextUid);

    @POST
    @Path("/projects/{projectId}/contexts/{contextUid}/match/async")
    AsyncProcessStartedPTO matchAsync(@PathParam("projectId") String projectId, @PathParam("contextUid") String contextUid,
        MatchRequestPTO matchRequestPTO);

    @POST
    @Path("/projects/{projectId}/contexts/upload-and-match-async")
    @Consumes (MediaType.MULTIPART_FORM_DATA)
    AsyncProcessStartedPTO uploadContextAndMatchAsync(@PathParam("projectId") String projectId,
        @MultipartForm ContextUploadAndMatchPTO contextUploadPTO);

    @GET
    @Path("/projects/{projectId}/processes/{processUid}")
    AsyncProcessPTO getAsyncProcess(@PathParam("projectId") String projectId, @PathParam("processUid") String processUid);

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
