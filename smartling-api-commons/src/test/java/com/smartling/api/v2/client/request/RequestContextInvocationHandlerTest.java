package com.smartling.api.v2.client.request;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RequestContextInvocationHandlerTest
{
    @Mock
    private InvocationHandler mockDelegate;

    @InjectMocks
    private RequestContextInvocationHandler handler;

    private Object mockProxy;
    private Method mockMethod;
    private Object[] mockArgs;

    @Before
    public void setUp() throws NoSuchMethodException
    {
        RequestContext context = new RequestContext("POST", URI.create("https://test.com"), new MultivaluedMapImpl<>());
        RequestContextHolder.setContext(context);

        mockProxy = new TestProxy();
        mockMethod = TestProxy.class.getMethod("foo");
        mockArgs = new Object[0];
    }

    @After
    public void tearDown()
    {
        RequestContextHolder.clearContext();
    }

    @Test
    public void shouldDelegateInvocationAndClearContext() throws Throwable
    {
        // Given
        final String expectedResult = "test result";
        when(mockDelegate.invoke(mockProxy, mockMethod, mockArgs)).thenReturn(expectedResult);
        
        // When
        Object result = handler.invoke(mockProxy, mockMethod, mockArgs);

        // Then
        assertEquals(expectedResult, result);
        verify(mockDelegate, times(1)).invoke(mockProxy, mockMethod, mockArgs);
        assertNull(RequestContextHolder.getContext());
    }

    @Test
    public void shouldClearContextEvenWhenDelegateThrowsException() throws Throwable
    {
        // Given
        when(mockDelegate.invoke(mockProxy, mockMethod, mockArgs)).thenThrow(new RuntimeException());

        // When-Then
        assertThrows(RuntimeException.class, () -> handler.invoke(mockProxy, mockMethod, mockArgs));

        verify(mockDelegate, times(1)).invoke(mockProxy, mockMethod, mockArgs);
        assertNull(RequestContextHolder.getContext());
    }

    private static class TestProxy
    {
        public void foo()
        {
        }
    }
}
