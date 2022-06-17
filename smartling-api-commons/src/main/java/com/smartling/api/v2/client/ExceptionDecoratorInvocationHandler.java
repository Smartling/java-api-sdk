package com.smartling.api.v2.client;

import com.smartling.api.v2.client.exception.RestApiExceptionHandler;
import com.smartling.api.v2.client.exception.server.DetailedErrorMessage;
import lombok.RequiredArgsConstructor;
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
            if (delegate.getClass().isAnnotationPresent(DetailedErrorMessage.class)) {
                if (!detailsBuilders.containsKey(method)) {
                    detailsBuilders.put(method, MessageDetailsBuilder.inspect(method));
                }
                List<MessageDetailsBuilder> detailsBuilders = this.detailsBuilders.get(method);
                StringBuilder errorDetails = new StringBuilder();
                for (MessageDetailsBuilder detailsBuilder : detailsBuilders) {
                    detailsBuilder.writeMessage(errorDetails, args);
                }
                throw handler.createRestApiException(ex, errorDetails.toString());
            } else {
                throw handler.createRestApiException(ex);
            }
        }
    }

    @RequiredArgsConstructor
    static class MessageDetailsBuilder
    {
        private final int argNumber;
        private final String argName;
        private final Field field;

        static List<MessageDetailsBuilder> inspect(final Method method)
        {
            List<MessageDetailsBuilder> result = new ArrayList<>();
            DetailedErrorMessage annotation = method.getAnnotation(DetailedErrorMessage.class);
            if (annotation == null) {
                annotation = method.getDeclaringClass().getAnnotation(DetailedErrorMessage.class);
            }
            Set<String> argNames = new HashSet<>(Arrays.asList(annotation.fields()));
            Annotation[][] parametersAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < parametersAnnotations.length && !argNames.isEmpty(); i++) {
                Annotation[] parameterAnnotations = parametersAnnotations[i];
                for (Annotation parameterAnnotation : parameterAnnotations) {
                    String argName;
                    if (parameterAnnotation instanceof PathParam) {
                        argName = ((PathParam) parameterAnnotation).value();
                        if (argNames.contains(argName)) {
                            result.add(new MessageDetailsBuilder(i, argName, null));
                            argNames.remove(argName);
                        }
                    } else if (parameterAnnotation instanceof QueryParam) {
                        argName = ((QueryParam) parameterAnnotation).value();
                        if (argNames.contains(argName)) {
                            result.add(new MessageDetailsBuilder(i, argName, null));
                            argNames.remove(argName);
                        }
                    } else {
                        Class<?> parameterType = method.getParameterTypes()[i];
                        for (Field field : FieldUtils.getAllFields(parameterType)) {
                            if (argNames.contains(field.getName())) {
                                result.add(new MessageDetailsBuilder(i, field.getName(), field));
                                argNames.remove(field.getName());
                            }
                        };
                    }
                }
            }
            return result;
        }

        void writeMessage(StringBuilder target, Object[] args)
        {
            try {
                Object arg = args[argNumber];
                if (field != null) {
                    arg = field.get(arg);
                }
                target.append(", ").append(argName).append("=").append(arg);
            } catch (Exception ex) {
                // skip
            }
        }
    }
}
