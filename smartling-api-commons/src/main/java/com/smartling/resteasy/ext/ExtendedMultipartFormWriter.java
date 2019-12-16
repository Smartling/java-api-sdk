package com.smartling.resteasy.ext;

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
        for (Field field : type.getDeclaredFields())
        {
            if (field.isAnnotationPresent(DynamicFormParam.class) && field.isAnnotationPresent(PartType.class))
            {
                AccessController.doPrivileged(new FieldEnablerPrivilegedAction(field));

                PartType partType = field.getAnnotation(PartType.class);

                Object value = safeExtractValue(obj, field);

                if (value instanceof Map)
                {
                    Map<Object, Object> directives = (Map<Object, Object>) value;

                    for (Map.Entry<Object, Object> directive : directives.entrySet())
                    {
                        if (directive.getKey() instanceof String &&
                            (directive.getValue() instanceof String ||
                             directive.getValue() instanceof Integer ||
                             directive.getValue() instanceof Boolean)
                        )
                        {
                            String directiveKey = (String) directive.getKey();
                            String directiveValue = directive.getValue().toString();

                            output.addFormData(directiveKey, directiveValue, String.class, String.class, MediaType.valueOf(partType.value()), null);
                        }
                    }
                }
            }
            if (field.isAnnotationPresent(ListFormParam.class) && field.isAnnotationPresent(PartType.class))
            {
                AccessController.doPrivileged(new FieldEnablerPrivilegedAction(field));

                ListFormParam param = field.getAnnotation(ListFormParam.class);
                PartType partType = field.getAnnotation(PartType.class);
                Object value = safeExtractValue(obj, field);

                if (value instanceof List)
                {
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
        }

        super.getFields(type, output, obj);
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
