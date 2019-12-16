package com.smartling.resteasy.ext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Must be used in conjunction with {@link org.jboss.resteasy.annotations.providers.multipart.PartType}
 * to mark lists of values for array fields.<br>
 *
 * Only{@code String} and {@code Integer} are allowed as values.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ListFormParam
    {
    /**
     * @see javax.ws.rs.FormParam#value
     */
    String value();
}
