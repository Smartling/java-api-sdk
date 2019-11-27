package com.smartling.api.v2.client;

import com.smartling.api.v2.client.exception.RestApiExceptionHandler;
import com.smartling.api.v2.client.exception.RestApiRuntimeException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


public class ExceptionDecoratorInvocationHandlerTest
{
    @Mock
    private Foo                     delegate;
    @Mock
    private RestApiExceptionHandler exceptionHandler;

    private Foo                     foo;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);

        foo = (Foo)Proxy.newProxyInstance(Foo.class.getClassLoader(), new Class[]{Foo.class}, new ExceptionDecoratorInvocationHandler<>(delegate, exceptionHandler));
    }

    @Test
    public void testBarSuccess() throws Exception
    {
        final Object obj = new Object();
        when(delegate.getBar()).thenReturn(obj);

        assertEquals(obj, foo.getBar());
    }

    @Test(expected = RestApiRuntimeException.class)
    public void testInvokeFail() throws Exception
    {
        when(delegate.getBar()).thenThrow(new RuntimeException());

        final RestApiRuntimeException restApiException = new RestApiRuntimeException(new RuntimeException());
        when(exceptionHandler.createRestApiException(any(InvocationTargetException.class))).thenReturn(restApiException);

        foo.getBar();
    }

    private interface Foo
    {
        Object getBar();
    }
}
