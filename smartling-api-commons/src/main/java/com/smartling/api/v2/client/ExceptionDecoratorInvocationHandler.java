package com.smartling.api.v2.client;

import com.smartling.api.v2.client.exception.RestApiExceptionHandler;
import com.smartling.api.v2.client.exception.server.DetailedErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides a invocation handler to translate invocation target exceptions into
 * API response exceptions.
 *
 * @param <T> the type being proxied
 */
@Slf4j
class ExceptionDecoratorInvocationHandler<T> implements InvocationHandler
{
    private final T delegate;
    private final RestApiExceptionHandler handler;
    private final Map<Method, List<MessageDetailsBuilder>> detailsBuilders = new HashMap<>();

    ExceptionDecoratorInvocationHandler(final T delegate, final RestApiExceptionHandler handler)
    {
        this.delegate = delegate;
        this.handler = handler;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable
    {
        try
        {
            return method.invoke(delegate, args);
        }
        catch (InvocationTargetException ex)
        {
            if (isAnnotationPresent(delegate.getClass(), DetailedErrorMessage.class)) {
                if (!detailsBuilders.containsKey(method)) {
                    detailsBuilders.put(method, inspect(method));
                }
                List<MessageDetailsBuilder> detailsBuilders = this.detailsBuilders.get(method);
                StringBuilder errorDetails = new StringBuilder(", method=").append(method.getName());
                for (MessageDetailsBuilder detailsBuilder : detailsBuilders) {
                    detailsBuilder.writeMessage(errorDetails, args);
                }
                throw handler.createRestApiException(ex, errorDetails.toString());
            } else {
                throw handler.createRestApiException(ex);
            }
        }
    }

    private List<MessageDetailsBuilder> inspect(Method method)
    {
        Set<String> argNames = new HashSet<>();
        DetailedErrorMessage annotation = method.getAnnotation(DetailedErrorMessage.class);
        if (annotation != null) {
            argNames.addAll(Arrays.asList(annotation.args()));
        }
        annotation = getAnnotationFromInterfaces(delegate.getClass(), DetailedErrorMessage.class);
        if (annotation != null) {
            argNames.addAll(Arrays.asList(annotation.args()));
        }
        if (argNames.isEmpty()) {
            return Collections.emptyList();
        }
        return lookupArgs(method, argNames);
    }

    private List<MessageDetailsBuilder> lookupArgs(Method method, Set<String> argNames) {
        List<MessageDetailsBuilder> result = new ArrayList<>();
        Annotation[][] parametersAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parametersAnnotations.length && !argNames.isEmpty(); i++) {
            Annotation[] parameterAnnotations = parametersAnnotations[i];
            String argName = null;
            PathParam path = getFromArray(parameterAnnotations, PathParam.class);
            if (path != null) {
                argName = path.value();
            } else {
                QueryParam query = getFromArray(parameterAnnotations, QueryParam.class);
                if (query != null) {
                    argName = query.value();
                }
            }
            if (argName == null) {
                Class<?> parameterType = method.getParameterTypes()[i];
                for (Field field : FieldUtils.getAllFields(parameterType)) {
                    if (argNames.contains(field.getName())) {
                        result.add(new MessageDetailsBuilder(i, field.getName(), field));
                        argNames.remove(field.getName());
                    }
                }
            } else {
                if (argNames.contains(argName)) {
                    result.add(new MessageDetailsBuilder(i, argName, null));
                    argNames.remove(argName);
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private <A extends Annotation> A getFromArray(Annotation[] parameterAnnotations, Class<A> annotationType) {
        for (Annotation parameterAnnotation : parameterAnnotations) {
            if (parameterAnnotation.annotationType() == annotationType) {
                return (A) parameterAnnotation;
            }
        }
        return null;
    }

    private <A extends Annotation> boolean isAnnotationPresent(Class<?> classType, final Class<A> annotationClass) {
        return getAnnotationFromInterfaces(classType, annotationClass) != null;
    }

    private <A extends Annotation> A getAnnotationFromInterfaces(Class<?> classType, final Class<A> annotationClass) {
        for (Class<?> anInterface : classType.getInterfaces()) {
            if (anInterface.isAnnotationPresent(annotationClass)) {
                return anInterface.getAnnotation(annotationClass);
            }
        }
        return classType.getAnnotation(annotationClass);
    }

    @ToString
    @RequiredArgsConstructor
    static class MessageDetailsBuilder
    {
        private final int argNumber;
        private final String argName;
        private final Field field;

        void writeMessage(StringBuilder target, Object[] args)
        {
            try {
                Object arg = args[argNumber];
                if (field != null) {
                    arg = FieldUtils.readField(field, arg, true);
                }
                target.append(", ").append(argName).append("=").append(arg);
            } catch (Exception ex) {
                log.warn("unable to process arg {} on {} position: ", argName, argNumber, ex);
            }
        }
    }
}
