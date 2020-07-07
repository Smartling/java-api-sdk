package com.smartling.resteasy.ext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Must be used in conjunction with {@link org.jboss.resteasy.annotations.providers.multipart.PartType}
 * to mark name-value Maps of dynamically defined fields.<br>
 *
 * Only String is allowed as a key, {@code String}, {@code Integer} or {@code Boolean} are allowed as values.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicFormParam
{
}
