package com.smartling.resteasy.ext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Must be used in conjunction with {@link org.jboss.resteasy.annotations.providers.multipart.PartType}.
 * It defines file form data with filename.
 * <p>
 * It is a replacement for {@link javax.ws.rs.FormParam} and {@link org.jboss.resteasy.annotations.jaxrs.FormParam}
 * and should not be used together on the same field.
 * <p>
 * Be careful, it may not fully replace FormParam functionality.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FileFormParam
{
    String value();
    String filenameField() default "";
}
