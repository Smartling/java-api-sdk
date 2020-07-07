package com.smartling.api.jobbatches.util;

import com.smartling.api.jobbatches.exceptions.JobBatchesApiExceptionMapper;
import com.smartling.api.v2.client.exception.RestApiExceptionHandler;
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
public class FileUploadProxyUtils
{
    private static final String CLIENT_LIB_ID = "smartling.client_lib_id";

    public static void addClientLibIdIfNeeded(MultipartFormDataOutput requestData)
    {
        if (!requestData.getFormData().containsKey(CLIENT_LIB_ID))
        {
            String clientLibId = LibNameVersionHolder.getClientLibName() + "/" + LibNameVersionHolder.getClientLibVersion();
            requestData.addFormData(CLIENT_LIB_ID, clientLibId, MediaType.TEXT_PLAIN_TYPE);
        }
    }

    public static Response sendRequest(ResteasyWebTarget client, String path, String projectId, String batchUid, MultipartFormDataOutput output)
    {
        RestApiExceptionHandler exceptionHandler = new RestApiExceptionHandler(new JobBatchesApiExceptionMapper());
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


    public static void getFields(Class<?> type, MultipartFormDataOutput output, Object obj)
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

    public static void releaseConnection(Response response)
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

    private static String getFilename(AccessibleObject method)
    {
        PartFilename fname = method.getAnnotation(PartFilename.class);
        return fname == null ? null : fname.value();
    }

    private static Object getFieldValue(Field field, Object obj)
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

    public static String getPathAnnotationValue(Class<?> clazz, String methodName, Class<?>... methodParameterTypes)
    {
        Method method;
        try
        {
            method = clazz.getMethod(methodName, methodParameterTypes);
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
