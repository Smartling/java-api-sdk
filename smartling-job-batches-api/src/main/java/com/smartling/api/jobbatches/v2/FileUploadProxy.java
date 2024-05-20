package com.smartling.api.jobbatches.v2;

import com.smartling.api.jobbatches.v2.pto.BatchPTO;
import com.smartling.api.jobbatches.v2.pto.BatchStatusResponsePTO;
import com.smartling.api.jobbatches.v2.pto.CancelBatchActionRequestPTO;
import com.smartling.api.jobbatches.v2.pto.CreateBatchRequestPTO;
import com.smartling.api.jobbatches.v2.pto.CreateBatchResponsePTO;
import com.smartling.api.jobbatches.v2.pto.CreateJobRequestPTO;
import com.smartling.api.jobbatches.v2.pto.CreateJobResponsePTO;
import com.smartling.api.jobbatches.v2.pto.FileUploadPTO;
import com.smartling.api.jobbatches.v2.pto.RegisterBatchActionRequestPTO;
import com.smartling.api.jobbatches.v2.pto.SearchParamsPTO;
import com.smartling.api.jobbatches.v2.pto.StreamFileUploadPTO;
import com.smartling.api.v2.response.ListResponse;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import javax.ws.rs.core.Response;

import static com.smartling.api.jobbatches.util.FileUploadProxyUtils.addClientLibIdIfNeeded;
import static com.smartling.api.jobbatches.util.FileUploadProxyUtils.getFields;
import static com.smartling.api.jobbatches.util.FileUploadProxyUtils.getPathAnnotationValue;
import static com.smartling.api.jobbatches.util.FileUploadProxyUtils.releaseConnection;
import static com.smartling.api.jobbatches.util.FileUploadProxyUtils.sendRequest;

@Slf4j
public class FileUploadProxy implements JobBatchesApi
{
    private JobBatchesApi delegate;
    private ResteasyWebTarget client;

    FileUploadProxy(JobBatchesApi delegate, ResteasyWebTarget client)
    {
        this.delegate = delegate;
        this.client = client;
    }

    @Override
    public CreateBatchResponsePTO createBatch(String projectId, CreateBatchRequestPTO createBatchRequest)
    {
        return delegate.createBatch(projectId, createBatchRequest);
    }

    @Override
    public BatchStatusResponsePTO getBatchStatus(String projectId, String batchUid)
    {
        return delegate.getBatchStatus(projectId, batchUid);
    }

    @Override
    public ListResponse<BatchPTO> listBatches(String projectId, SearchParamsPTO searchParams)
    {
        return delegate.listBatches(projectId, searchParams);
    }

    @Override
    public void addFileAsync(String projectId, String batchUid, FileUploadPTO fileUploadPTO)
    {
        MultipartFormDataOutput output = new MultipartFormDataOutput();
        getFields(FileUploadPTO.class, output, fileUploadPTO);
        addClientLibIdIfNeeded(output);

        String path = getPathAnnotationValue(JobBatchesApi.class,"addFileAsync", String.class, String.class, FileUploadPTO.class);
        Response response = sendRequest(client, path, projectId, batchUid, output);
        releaseConnection(response);
    }

    @Override
    public void addFileAsStreamAsync(String projectId, String batchUid, StreamFileUploadPTO streamFileUploadPTO)
    {
        MultipartFormDataOutput output = new MultipartFormDataOutput();
        getFields(StreamFileUploadPTO.class, output, streamFileUploadPTO);
        addClientLibIdIfNeeded(output);

        String path = getPathAnnotationValue(JobBatchesApi.class,"addFileAsStreamAsync", String.class, String.class, StreamFileUploadPTO.class);
        Response response = sendRequest(client, path, projectId, batchUid, output);
        releaseConnection(response);
    }

    @Override
    public void registerFile(String projectId, String batchUid, RegisterBatchActionRequestPTO request) {
        delegate.registerFile(projectId, batchUid, request);
    }

    @Override
    public void cancelFile(String projectId, String batchUid, CancelBatchActionRequestPTO request) {
        delegate.cancelFile(projectId, batchUid, request);
    }

    @Override
    public CreateJobResponsePTO createJob(String projectId, CreateJobRequestPTO createJobRequest) {
        return delegate.createJob(projectId, createJobRequest);
    }
}
