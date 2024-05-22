package com.smartling.api.jobbatches.v2;

import com.smartling.api.jobbatches.v2.pto.BatchStatusResponsePTO;
import com.smartling.api.jobbatches.v2.pto.CancelBatchActionRequestPTO;
import com.smartling.api.jobbatches.v2.pto.CreateBatchRequestPTO;
import com.smartling.api.jobbatches.v2.pto.CreateBatchResponsePTO;
import com.smartling.api.jobbatches.v2.pto.CreateJobRequestPTO;
import com.smartling.api.jobbatches.v2.pto.CreateJobResponsePTO;
import com.smartling.api.jobbatches.v2.pto.RegisterBatchActionRequestPTO;
import com.smartling.api.jobbatches.v2.pto.StreamFileUploadPTO;
import com.smartling.api.jobbatches.v2.pto.BatchPTO;
import com.smartling.api.jobbatches.v2.pto.FileUploadPTO;
import com.smartling.api.jobbatches.v2.pto.SearchParamsPTO;
import com.smartling.api.v2.response.ListResponse;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface JobBatchesApi
{
    @POST
    @Path("job-batches-api/v2/projects/{projectId}/batches")
    CreateBatchResponsePTO createBatch(@PathParam("projectId") String projectId, CreateBatchRequestPTO createBatchRequest);

    @GET
    @Path("job-batches-api/v2/projects/{projectId}/batches/{batchUid}")
    BatchStatusResponsePTO getBatchStatus(@PathParam("projectId") String projectId, @PathParam("batchUid") String batchUid);

    @GET
    @Path("job-batches-api/v2/projects/{projectId}/batches")
    ListResponse<BatchPTO> listBatches(@PathParam("projectId") String projectId, @BeanParam SearchParamsPTO searchParams);

    @POST
    @Path("job-batches-api/v2/projects/{projectId}/batches/{batchUid}/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void addFileAsync(@PathParam("projectId") String projectId, @PathParam("batchUid") String batchUid, @MultipartForm FileUploadPTO fileUploadPTO);

    @POST
    @Path("job-batches-api/v2/projects/{projectId}/batches/{batchUid}/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void addFileAsStreamAsync(@PathParam("projectId") String projectId, @PathParam("batchUid") String batchUid, @MultipartForm StreamFileUploadPTO streamFileUploadPTO);

    @PUT
    @Path("job-batches-api/v2/projects/{projectId}/batches/{batchUid}")
    void registerFile(@PathParam("projectId") String projectId, @PathParam("batchUid") String batchUid, RegisterBatchActionRequestPTO request);

    @PUT
    @Path("job-batches-api/v2/projects/{projectId}/batches/{batchUid}")
    void cancelFile(@PathParam("projectId") String projectId, @PathParam("batchUid") String batchUid, CancelBatchActionRequestPTO request);

    @POST
    @Path("job-batches-api/v2/projects/{projectId}/jobs")
    CreateJobResponsePTO createJob(@PathParam("projectId") String projectId, CreateJobRequestPTO createJobRequest);
}
