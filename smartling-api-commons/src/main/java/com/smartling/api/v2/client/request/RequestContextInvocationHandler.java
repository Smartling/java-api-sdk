package com.smartling.api.v2.client.request;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class RequestContextInvocationHandler implements InvocationHandler
{
    private final InvocationHandler delegate;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        try
        {
            return delegate.invoke(proxy, method, args);
        }
        finally
        {
            RequestContextHolder.clearContext();
        }
    }
}
