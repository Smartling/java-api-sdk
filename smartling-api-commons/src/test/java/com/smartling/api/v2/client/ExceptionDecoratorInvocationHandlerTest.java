package com.smartling.api.v2.client;

import com.smartling.api.v2.client.exception.RestApiExceptionHandler;
import com.smartling.api.v2.client.exception.RestApiRuntimeException;
import com.smartling.api.v2.client.exception.server.DetailedErrorMessage;
import lombok.AllArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class ExceptionDecoratorInvocationHandlerTest
{
    private static final String ID = "100500";
    private static final TestDto DTO = new TestDto(ID);
    private static final Search SEARCH = new Search("name", "namespace");

    @Mock
    private TestApi delegate;
    @Mock
    private RestApiExceptionHandler exceptionHandler;

    private TestApi testApi;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);

        testApi = (TestApi)Proxy.newProxyInstance(TestApi.class.getClassLoader(), new Class[]{TestApi.class}, new ExceptionDecoratorInvocationHandler<>(delegate, exceptionHandler));
        final RestApiRuntimeException restApiException = new RestApiRuntimeException(new RuntimeException());
        when(exceptionHandler.createRestApiException(any(InvocationTargetException.class), anyString())).thenReturn(restApiException);
    }

    @Test
    public void testInvokeSuccess() throws Exception
    {
        when(delegate.getItem(ID)).thenReturn(DTO);
        assertEquals(DTO, testApi.getItem(ID));
    }

    @Test
    public void testInvokeFailWithQueryParamMessage() throws Exception
    {
        when(delegate.getItem(ID)).thenThrow(new RuntimeException());
        assertThrows(RestApiRuntimeException.class, new ThrowingRunnable() {
            @Override
            public void run() {
                testApi.getItem(ID);
            }
        });
        verify(exceptionHandler).createRestApiException(any(InvocationTargetException.class), eq(", method=getItem, id=100500"));
    }

    @Test
    public void testInvokeFailWithPathParamMessage() throws Exception
    {
        when(delegate.getItemStatus(ID)).thenThrow(new RuntimeException());
        assertThrows(RestApiRuntimeException.class, new ThrowingRunnable() {
            @Override
            public void run() {
                testApi.getItemStatus(ID);
            }
        });
        verify(exceptionHandler).createRestApiException(any(InvocationTargetException.class), eq(", method=getItemStatus, id=100500"));
    }

    @Test
    public void testInvokeFailWithDtoParamMessage() throws Exception
    {
        when(delegate.updateItem(DTO)).thenThrow(new RuntimeException());
        assertThrows(RestApiRuntimeException.class, new ThrowingRunnable() {
            @Override
            public void run() {
                testApi.updateItem(DTO);
            }
        });
        verify(exceptionHandler).createRestApiException(any(InvocationTargetException.class), eq(", method=updateItem, id=100500"));
    }

    @Test
    public void testInvokeFailWithNoArgsMessage() throws Exception
    {
        when(delegate.getIdByName("name")).thenThrow(new RuntimeException());
        assertThrows(RestApiRuntimeException.class, new ThrowingRunnable() {
            @Override
            public void run() {
                testApi.getIdByName("name");
            }
        });
        verify(exceptionHandler).createRestApiException(any(InvocationTargetException.class), eq(", method=getIdByName"));
    }

    @Test
    public void testInvokeFailWithMultipleDetailsMessage() throws Exception
    {
        when(delegate.search(ID, "name")).thenThrow(new RuntimeException());
        assertThrows(RestApiRuntimeException.class, new ThrowingRunnable() {
            @Override
            public void run() {
                testApi.search(ID, "name");
            }
        });
        verify(exceptionHandler).createRestApiException(any(InvocationTargetException.class), eq(", method=search, id=100500, name=name"));
    }

    @Test
    public void testInvokeFailWithQueryAndDtoParamsMessage() throws Exception
    {
        when(delegate.createItem("namespace", DTO)).thenThrow(new RuntimeException());
        assertThrows(RestApiRuntimeException.class, new ThrowingRunnable() {
            @Override
            public void run() {
                testApi.createItem("namespace", DTO);
            }
        });
        verify(exceptionHandler).createRestApiException(any(InvocationTargetException.class), eq(", method=createItem, id=100500"));
    }

    @Test
    public void testInvokeFailWithBeanParamsMessage() throws Exception
    {
        when(delegate.search(ID, true, SEARCH)).thenThrow(new RuntimeException());
        assertThrows(RestApiRuntimeException.class, new ThrowingRunnable() {
            @Override
            public void run() {
                testApi.search(ID, true, SEARCH);
            }
        });
        verify(exceptionHandler).createRestApiException(any(InvocationTargetException.class), eq(", method=search, id=100500, name=name, ns=namespace"));
    }

    @DetailedErrorMessage(args = "id")
    private interface TestApi
    {
        TestDto getItem(@QueryParam("id") String id);

        TestDto updateItem(TestDto dto);

        TestDto createItem(@QueryParam("ns") String namespace, TestDto dto);

        @Path("/{id}/status")
        String getItemStatus(@PathParam("id") String id);

        String getIdByName(@QueryParam("name") String name);

        @DetailedErrorMessage(args = "name")
        TestDto search(@QueryParam("id") String id, @QueryParam("name") String name);

        @DetailedErrorMessage(args = {"name", "ns"})
        TestDto search(@QueryParam("id") String id, @QueryParam("recursive") Boolean recursive, @BeanParam Search search);
    }

    @AllArgsConstructor
    private static class TestDto
    {
        private String id;
    }

    @AllArgsConstructor
    private static class Search
    {
        private String name;
        private String ns;
    }
}
