package com.smartling.api.attachments.v2;

import com.smartling.api.attachments.v2.pto.AttachmentPTO;
import com.smartling.api.attachments.v2.pto.AttachmentUploadPTO;
import com.smartling.api.v2.response.ListResponse;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.io.InputStream;

import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static javax.ws.rs.core.MediaType.WILDCARD;

@Path("/attachments-api/v2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AttachmentsApi extends AutoCloseable
{
    @GET
    @Path("/accounts/{accountUid}/{type}/{entityUid}")
    ListResponse<AttachmentPTO> entityAttachments(@PathParam("accountUid") String accountUid, @PathParam("type") String type, @PathParam("entityUid") String entityUid);

    @POST
    @Path("/accounts/{accountUid}/{type}/attachments")
    @Consumes(MULTIPART_FORM_DATA)
    AttachmentPTO uploadAttachment(@PathParam("accountUid") String accountUid, @PathParam("type") String type, @MultipartForm AttachmentUploadPTO attachmentUploadPTO);

    @GET
    @Path("/accounts/{accountUid}/{type}/attachments/{attachmentUid}")
    @Produces(WILDCARD)
    InputStream downloadAttachment(@PathParam("accountUid") String accountUid, @PathParam("type") String type, @PathParam("attachmentUid") String attachmentUid);
}
