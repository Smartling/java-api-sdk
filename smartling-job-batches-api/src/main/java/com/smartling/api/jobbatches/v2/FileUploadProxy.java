package com.smartling.api.jobbatches.v2;

import com.smartling.api.jobbatches.v2.pto.BatchActionRequestPTO;
import com.smartling.api.jobbatches.v2.pto.BatchStatusResponsePTO;
import com.smartling.api.jobbatches.v2.pto.CreateBatchRequestPTO;
import com.smartling.api.jobbatches.v2.pto.CreateBatchResponsePTO;
import com.smartling.api.jobbatches.v2.pto.StreamFileUploadPTO;
import com.smartling.api.v2.client.exception.RestApiExceptionHandler;
import com.smartling.api.jobbatches.v2.pto.BatchPTO;
import com.smartling.api.jobbatches.v2.pto.FileUploadPTO;
import com.smartling.api.jobbatches.v2.pto.SearchParamsPTO;
import com.smartling.api.v2.response.ListResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.jboss.resteasy.annotations.providers.multipart.PartFilename;
import org.jboss.resteasy.annotations.providers.multipart.PartType;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ClientResponse;
import org.jboss.resteasy.plugins.providers.multipart.FieldEnablerPrivilegedAction;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.Map;

@Slf4j
public class FileUploadProxy implements JobFacadeApi
{
//    public static final String CLIENT_LIB_ID = "smartling.client_lib_id";

    private JobFacadeApi delegate;
    private ResteasyWebTarget client;

    FileUploadProxy(JobFacadeApi delegate, ResteasyWebTarget client)
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
    public void addFile(String projectId, String batchUid, FileUploadPTO fileUploadPTO)
    {
        MultipartFormDataOutput output = new MultipartFormDataOutput();
        getFields(FileUploadPTO.class, output, fileUploadPTO);
//        addClientLibIdIfNeeded(output);

        String path = getPathAnnotationValue("addFile", String.class, String.class, FileUploadPTO.class);
        Response response = sendRequest(path, projectId, batchUid, output);
        releaseConnection(response);
    }

    @Override
    public void addFileAsStream(String projectId, String batchUid, StreamFileUploadPTO streamFileUploadPTO)
    {
        MultipartFormDataOutput output = new MultipartFormDataOutput();
        getFields(StreamFileUploadPTO.class, output, streamFileUploadPTO);
//        addClientLibIdIfNeeded(output);

        String path = getPathAnnotationValue("addFileAsStream", String.class, String.class, StreamFileUploadPTO.class);
        Response response = sendRequest(path, projectId, batchUid, output);
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
//        addClientLibIdIfNeeded(output);

        String path = getPathAnnotationValue("addFileAsync", String.class, String.class, FileUploadPTO.class);
        Response response = sendRequest(path, projectId, batchUid, output);
        releaseConnection(response);
    }

    @Override
    public void addFileAsStreamAsync(String projectId, String batchUid, StreamFileUploadPTO streamFileUploadPTO)
    {
        MultipartFormDataOutput output = new MultipartFormDataOutput();
        getFields(StreamFileUploadPTO.class, output, streamFileUploadPTO);
//        addClientLibIdIfNeeded(output);

        String path = getPathAnnotationValue("addFileAsStreamAsync", String.class, String.class, StreamFileUploadPTO.class);
        Response response = sendRequest(path, projectId, batchUid, output);
        releaseConnection(response);
    }

//    private void addClientLibIdIfNeeded(MultipartFormDataOutput requestData)
//    {
//        if (!requestData.getFormData().containsKey(CLIENT_LIB_ID))
//        {
//            String clientLibId = LibNameVersionHolder.getClientLibName() + "/" + LibNameVersionHolder.getClientLibVersion();
//            requestData.addFormData(CLIENT_LIB_ID, clientLibId, MediaType.TEXT_PLAIN_TYPE);
//        }
//    }

    private Response sendRequest(String path, String projectId, String batchUid, MultipartFormDataOutput output)
    {
        RestApiExceptionHandler exceptionHandler = new RestApiExceptionHandler();
        try
        {
            Response response = client.path(path)
                .resolveTemplate("projectId", projectId)
                .resolveTemplate("batchUid", batchUid)
                .request()
                .post(Entity.entity(output, MediaType.MULTIPART_FORM_DATA));
            if (response.getStatus() != HttpStatus.SC_ACCEPTED)
            {
                throw new WebApplicationException(response);
            }
            return response;
        }
        catch (WebApplicationException e)
        {
            throw exceptionHandler.createRestApiException(e);
        }
    }

    private void getFields(Class<?> type, MultipartFormDataOutput output, Object obj)
    {
        for (Field field : type.getDeclaredFields())
        {
            AccessController.doPrivileged(new FieldEnablerPrivilegedAction(field));
            Object value = getFieldValue(field, obj);
            if (value == null)
            {
                continue;
            }
            if (field.isAnnotationPresent(FormParam.class) && field.isAnnotationPresent(PartType.class))
            {
                FormParam param = field.getAnnotation(FormParam.class);
                PartType partType = field.getAnnotation(PartType.class);
                String filename = getFilename(field);

                output.addFormData(param.value(), value, field.getType(), field.getGenericType(), MediaType.valueOf(partType.value()), filename);
            }
            if (field.getType().isAssignableFrom(Map.class))
            {
                Map<String, String> directives = (Map<String, String>) getFieldValue(field, obj);
                if (directives == null)
                {
                    continue;
                }
                for (Map.Entry<String, String> entry : directives.entrySet())
                {
                    if (entry.getValue() == null)
                    {
                        continue;
                    }
                    output.addFormData(entry.getKey(), entry.getValue(), MediaType.TEXT_PLAIN_TYPE);
                }
            }
        }
    }

    private void releaseConnection(Response response)
    {
        if (response instanceof ClientResponse)
        {
            try
            {
                ((ClientResponse) response).releaseConnection();
            }
            catch (IOException e)
            {
                log.warn("Failed to release connection for response", e);
            }
        }
    }

    private String getFilename(AccessibleObject method)
    {
        PartFilename fname = method.getAnnotation(PartFilename.class);
        return fname == null ? null : fname.value();
    }

    private Object getFieldValue(Field field, Object obj)
    {
        try
        {
            return field.get(obj);
        }
        catch (IllegalAccessException e)
        {
            return null;
        }
    }

    private String getPathAnnotationValue(String methodName, Class<?>... methodParameterTypes)
    {
        Method method;
        try
        {
            method = JobFacadeApi.class.getMethod(methodName, methodParameterTypes);
            if (method.isAnnotationPresent(Path.class))
            {
                return method.getAnnotation(Path.class).value();
            }
        }
        catch (NoSuchMethodException e)
        {
            return "";
        }
        return "";
    }
}
