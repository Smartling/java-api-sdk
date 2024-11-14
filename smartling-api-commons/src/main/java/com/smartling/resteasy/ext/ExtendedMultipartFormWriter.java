package com.smartling.resteasy.ext;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.annotations.providers.multipart.PartType;
import org.jboss.resteasy.plugins.providers.multipart.FieldEnablerPrivilegedAction;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormAnnotationWriter;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.jboss.resteasy.spi.WriterException;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.util.List;
import java.util.Map;

/**
 * Provides additional functionality for {@code @MultipartForm} annotated parameter objects.
 *
 * <ul>
 *     <li>Extracts any {@link Map} parameter annotated with {@link DynamicFormParam} and {@link PartType}
 *     into a number of multipart fields.</li>
 *     <li>Extracts any {@link List} annotated with {@link ListFormParam} and {@link PartType}
 *     into an array of multipart form fields.</li>
 * </ul>
 *
 */
@Provider
@Produces("multipart/form-data")
public class ExtendedMultipartFormWriter extends MultipartFormAnnotationWriter
{
    @Override
    protected void getFields(Class<?> type, MultipartFormDataOutput output, Object obj) throws IOException
    {
        Field[] declaredFields = type.getDeclaredFields();
        for (Field field : declaredFields)
        {
            if (field.isAnnotationPresent(DynamicFormParam.class) && field.isAnnotationPresent(PartType.class))
            {
                AccessController.doPrivileged(new FieldEnablerPrivilegedAction(field));

                Object value = safeExtractValue(obj, field);

                if (value instanceof Map)
                {
                    PartType partType = field.getAnnotation(PartType.class);

                    Map<Object, Object> dynamicFields = (Map<Object, Object>) value;

                    for (Map.Entry<Object, Object> dynamicField : dynamicFields.entrySet())
                    {
                        if (dynamicField.getKey() instanceof String &&
                            (dynamicField.getValue() instanceof String ||
                             dynamicField.getValue() instanceof Integer ||
                             dynamicField.getValue() instanceof Boolean)
                        )
                        {
                            String fieldKey = (String) dynamicField.getKey();
                            String fieldValue = dynamicField.getValue().toString();

                            output.addFormData(fieldKey, fieldValue, String.class, String.class, MediaType.valueOf(partType.value()), null);
                        }
                    }
                }
            }
            if (field.isAnnotationPresent(ListFormParam.class) && field.isAnnotationPresent(PartType.class))
            {
                AccessController.doPrivileged(new FieldEnablerPrivilegedAction(field));
                Object value = safeExtractValue(obj, field);

                if (value instanceof List)
                {
                    ListFormParam param = field.getAnnotation(ListFormParam.class);
                    PartType partType = field.getAnnotation(PartType.class);

                    List<Object> array = (List<Object>) value;

                    for (Object item : array)
                    {
                        if (item instanceof String || item instanceof Integer)
                        {
                            output.addFormData(param.value(), item.toString(), String.class, String.class, MediaType.valueOf(partType.value()), null);
                        }
                    }
                }
            }
            if (field.isAnnotationPresent(FileFormParam.class) && field.isAnnotationPresent(PartType.class))
            {
                AccessController.doPrivileged(new FieldEnablerPrivilegedAction(field));

                FileFormParam fileFormParam = field.getAnnotation(FileFormParam.class);
                String name = fileFormParam.value();
                Object value = safeExtractValue(obj, field);
                String mediaType = field.getAnnotation(PartType.class).value();
                String filename = getFilename(obj, declaredFields, field);

                output.addFormData(name, value, field.getType(), field.getGenericType(), MediaType.valueOf(mediaType), filename);
            }
        }

        super.getFields(type, output, obj);
    }

    private String getFilename(Object obj, Field[] declaredFields, Field field)
    {
        String filenameField = field.getAnnotation(FileFormParam.class).filenameField();
        if (StringUtils.isNotEmpty(filenameField))
        {
            for (Field declaredField : declaredFields)
            {
                if (declaredField.getName().equals(filenameField))
                {
                    AccessController.doPrivileged(new FieldEnablerPrivilegedAction(declaredField));

                    return (String) safeExtractValue(obj, declaredField);
                }
            }
        }

        return getFilename(field);
    }

    private Object safeExtractValue(Object obj, Field field)
    {
        Object value;
        try
        {
            value = field.get(obj);
        }
        catch (IllegalAccessException e)
        {
            throw new WriterException(e);
        }
        return value;
    }
}
