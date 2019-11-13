package com.smartling.api.v2.jobbatches;

import com.smartling.api.v2.jobbatches.pto.BatchActionRequestPTO;
import com.smartling.api.v2.jobbatches.pto.BatchPTO;
import com.smartling.api.v2.jobbatches.pto.BatchStatusResponsePTO;
import com.smartling.api.v2.jobbatches.pto.CreateBatchRequestPTO;
import com.smartling.api.v2.jobbatches.pto.CreateBatchResponsePTO;
import com.smartling.api.v2.jobbatches.pto.FileUploadPTO;
import com.smartling.api.v2.jobbatches.pto.SearchParamsPTO;
import com.smartling.api.v2.jobbatches.pto.StreamFileUploadPTO;
import com.smartling.api.v2.response.ListResponse;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface JobFacadeApi
{
    @POST
    @Path("jobs-batch-api/v1/projects/{projectId}/batches")
    CreateBatchResponsePTO createBatch(@PathParam("projectId") String projectId, CreateBatchRequestPTO createBatchRequest);

    @GET
    @Path("jobs-batch-api/v1/projects/{projectId}/batches/{batchUid}")
    BatchStatusResponsePTO getBatchStatus(@PathParam("projectId") String projectId, @PathParam("batchUid") String batchUid);

    @GET
    @Path("jobs-batch-api/v1/projects/{projectId}/batches")
    ListResponse<BatchPTO> listBatches(@PathParam("projectId") String projectId, @BeanParam SearchParamsPTO searchParams);

    @POST
    @Path("jobs-batch-api/v1/projects/{projectId}/batches/{batchUid}/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void addFile(@PathParam("projectId") String projectId, @PathParam("batchUid") String batchUid, @MultipartForm FileUploadPTO fileUploadPTO);

    @POST
    @Path("jobs-batch-api/v1/projects/{projectId}/batches/{batchUid}/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void addFileAsStream(@PathParam("projectId") String projectId, @PathParam("batchUid") String batchUid, @MultipartForm StreamFileUploadPTO streamFileUploadPTO);

    @POST
    @Path("jobs-batch-api/v2/projects/{projectId}/batches/{batchUid}/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void addFileAsync(@PathParam("projectId") String projectId, @PathParam("batchUid") String batchUid, @MultipartForm FileUploadPTO fileUploadPTO);

    @POST
    @Path("jobs-batch-api/v2/projects/{projectId}/batches/{batchUid}/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void addFileAsStreamAsync(@PathParam("projectId") String projectId, @PathParam("batchUid") String batchUid, @MultipartForm StreamFileUploadPTO streamFileUploadPTO);

    @POST
    @Path("jobs-batch-api/v1/projects/{projectId}/batches/{batchUid}")
    void executeBatch(@PathParam("projectId") String projectId, @PathParam("batchUid") String batchUid, BatchActionRequestPTO request);
}
