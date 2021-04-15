package com.smartling.api.attachments.v2;

import com.smartling.api.attachments.v2.pto.AttachmentPTO;
import com.smartling.api.v2.response.ListResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/attachments-api/v2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AttachmentsApi
{
    @GET
    @Path("/accounts/{accountUid}/{type}/{entityUid}")
    ListResponse<AttachmentPTO> entityAttachments(@PathParam("accountUid") String accountUid, @PathParam("type") String type, @PathParam("entityUid") String entityUid);
}
