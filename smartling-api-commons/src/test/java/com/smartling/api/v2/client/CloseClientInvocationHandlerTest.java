package com.smartling.api.v2.client;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.client.Client;
import java.io.Closeable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CloseClientInvocationHandlerTest
{
    @Mock
    private InvocationHandler subhandler;
    @Mock
    private Client client;
    @InjectMocks
    private CloseClientInvocationHandler testInstance;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSubhandlerCall() throws Throwable
    {
        when(subhandler.invoke(any(), any(), any())).thenReturn(2);

        TestApi testApi = (TestApi)Proxy.newProxyInstance(TestApi.class.getClassLoader(), new Class[]{TestApi.class}, testInstance);
        testApi.add(1);

        verify(subhandler).invoke(any(), any(), any());
    }

    @Test
    public void testClose() throws Throwable
    {
        TestApi testApi = (TestApi)Proxy.newProxyInstance(TestApi.class.getClassLoader(), new Class[]{TestApi.class}, testInstance);
        testApi.close();

        verify(client).close();
        verify(subhandler, never()).invoke(any(), any(), any());
    }

    @Test
    public void testCloseWithParam() throws Throwable
    {
        when(subhandler.invoke(any(), any(), any())).thenReturn(null);

        TestApi testApi = (TestApi)Proxy.newProxyInstance(TestApi.class.getClassLoader(), new Class[]{TestApi.class}, testInstance);
        testApi.close(1);

        verify(subhandler).invoke(any(), any(), any());
        verify(client, never()).close();
    }

    @Test
    public void testCloseable() throws Exception
    {
        TestCloseableApi testApi = (TestCloseableApi)Proxy.newProxyInstance(TestCloseableApi.class.getClassLoader(), new Class[]{TestCloseableApi.class}, testInstance);
        testApi.close();

        verify(client).close();
    }

    @Test
    public void testAutoCloseable() throws Exception
    {
        TestAutoCloseableApi testApi = (TestAutoCloseableApi)Proxy.newProxyInstance(TestAutoCloseableApi.class.getClassLoader(), new Class[]{TestAutoCloseableApi.class}, testInstance);
        testApi.close();

        verify(client).close();
    }

    private interface TestApi
    {
        int add(int a);
        void close();
        void close(int a);
    }

    private interface TestCloseableApi extends Closeable
    {
        int add(int a);
    }

    private interface TestAutoCloseableApi extends AutoCloseable
    {
        int add(int a);
    }
}
