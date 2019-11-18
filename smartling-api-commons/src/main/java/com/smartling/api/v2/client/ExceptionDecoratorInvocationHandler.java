package com.smartling.api.v2.client;

import com.smartling.api.v2.client.exception.RestApiExceptionHandler;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
            throw handler.createRestApiException(ex);
        }
    }
}
