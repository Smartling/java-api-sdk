package com.smartling.api.v2.client.directives;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Must be used in conjunction with {@link org.jboss.resteasy.annotations.providers.multipart.PartType}
 * to mark directives Maps for uploading files.
 * <br/>
 * Only String is allowed as a key, String, Integer or Boolean are allowed as values
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Directives
{
    String prefix() default "";
}
