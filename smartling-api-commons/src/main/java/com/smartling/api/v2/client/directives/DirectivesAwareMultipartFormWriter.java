package com.smartling.api.v2.client.directives;

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
import java.util.Map;

@Provider
@Produces("multipart/form-data")
public class DirectivesAwareMultipartFormWriter extends MultipartFormAnnotationWriter
{
    @Override
    protected void getFields(Class<?> type, MultipartFormDataOutput output, Object obj) throws IOException
    {
        for (Field field : type.getDeclaredFields())
        {
            if (field.isAnnotationPresent(Directives.class) && field.isAnnotationPresent(PartType.class))
            {
                AccessController.doPrivileged(new FieldEnablerPrivilegedAction(field));
                Directives directivesAnnotation = field.getAnnotation(Directives.class);
                String prefix = directivesAnnotation.prefix();

                Object value = null;
                try
                {
                    value = field.get(obj);
                }
                catch (IllegalAccessException e)
                {
                    throw new WriterException(e);
                }

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

                            String directiveName = prefix + directiveKey;

                            PartType partType = field.getAnnotation(PartType.class);

                            output.addFormData(directiveName, directiveValue, String.class, String.class, MediaType.valueOf(partType.value()), null);
                        }
                    }
                }
            }
        }

        super.getFields(type, output, obj);
    }
}
