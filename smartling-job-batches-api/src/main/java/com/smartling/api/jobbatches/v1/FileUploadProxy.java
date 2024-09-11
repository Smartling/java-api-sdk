package com.smartling.api.jobbatches.v1;

import com.smartling.api.jobbatches.v1.pto.BatchActionRequestPTO;
import com.smartling.api.jobbatches.v1.pto.BatchPTO;
import com.smartling.api.jobbatches.v1.pto.BatchStatusResponsePTO;
import com.smartling.api.jobbatches.v1.pto.CreateBatchRequestPTO;
import com.smartling.api.jobbatches.v1.pto.CreateBatchResponsePTO;
import com.smartling.api.jobbatches.v1.pto.FileUploadPTO;
import com.smartling.api.jobbatches.v1.pto.SearchParamsPTO;
import com.smartling.api.jobbatches.v1.pto.StreamFileUploadPTO;
import com.smartling.api.v2.response.ListResponse;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
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
    private final JobBatchesApi delegate;
    private final ResteasyClient client;
    private final ResteasyWebTarget target;

    FileUploadProxy(JobBatchesApi delegate, ResteasyClient client, ResteasyWebTarget target)
    {
        this.delegate = delegate;
        this.client = client;
        this.target = target;
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
    public void addFile(String projectId, String batchUid, FileUploadPTO fileUploadPTO)
    {
        MultipartFormDataOutput output = new MultipartFormDataOutput();
        getFields(FileUploadPTO.class, output, fileUploadPTO);
        addClientLibIdIfNeeded(output);

        String path = getPathAnnotationValue(JobBatchesApi.class, "addFile", String.class, String.class, FileUploadPTO.class);
        Response response = sendRequest(target, path, projectId, batchUid, output);
        releaseConnection(response);
    }

    @Override
    public void addFileAsStream(String projectId, String batchUid, StreamFileUploadPTO streamFileUploadPTO)
    {
        MultipartFormDataOutput output = new MultipartFormDataOutput();
        getFields(StreamFileUploadPTO.class, output, streamFileUploadPTO);
        addClientLibIdIfNeeded(output);

        String path = getPathAnnotationValue(JobBatchesApi.class, "addFileAsStream", String.class, String.class, StreamFileUploadPTO.class);
        Response response = sendRequest(target, path, projectId, batchUid, output);
        releaseConnection(response);
    }

    @Override
    public void executeBatch(String projectId, String batchUid, BatchActionRequestPTO request)
    {
        delegate.executeBatch(projectId, batchUid, request);
    }

    @Override
    public void addFileAsync(String projectId, String batchUid, FileUploadPTO fileUploadPTO)
    {
        MultipartFormDataOutput output = new MultipartFormDataOutput();
        getFields(FileUploadPTO.class, output, fileUploadPTO);
        addClientLibIdIfNeeded(output);

        String path = getPathAnnotationValue(JobBatchesApi.class, "addFileAsync", String.class, String.class, FileUploadPTO.class);
        Response response = sendRequest(target, path, projectId, batchUid, output);
        releaseConnection(response);
    }

    @Override
    public void addFileAsStreamAsync(String projectId, String batchUid, StreamFileUploadPTO streamFileUploadPTO)
    {
        MultipartFormDataOutput output = new MultipartFormDataOutput();
        getFields(StreamFileUploadPTO.class, output, streamFileUploadPTO);
        addClientLibIdIfNeeded(output);

        String path = getPathAnnotationValue(JobBatchesApi.class, "addFileAsStreamAsync", String.class, String.class, StreamFileUploadPTO.class);
        Response response = sendRequest(target, path, projectId, batchUid, output);
        releaseConnection(response);
    }

    @Override
    public void close() throws Exception
    {
        client.close();
        delegate.close();
    }
}
