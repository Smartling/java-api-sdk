package com.smartling.api.external.client.proxy;

import com.smartling.api.external.client.exception.RestApiExceptionHandler;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ExceptionDecoratorInvocationHandler<T> implements InvocationHandler
{
    private final T delegate;
    private final RestApiExceptionHandler handler;

    public ExceptionDecoratorInvocationHandler(final T delegate, final RestApiExceptionHandler handler)
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
