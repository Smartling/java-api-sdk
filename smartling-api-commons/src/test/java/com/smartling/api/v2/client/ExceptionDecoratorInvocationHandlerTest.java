package com.smartling.api.v2.client;

import com.smartling.api.v2.client.exception.RestApiExceptionHandler;
import com.smartling.api.v2.client.exception.RestApiRuntimeException;
import com.smartling.api.v2.client.exception.server.DetailedErrorMessage;
import lombok.AllArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class ExceptionDecoratorInvocationHandlerTest
{
    private static final String ID = "100500";
    private static final TestDto DTO = new TestDto(ID);

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

    @Test(expected = RestApiRuntimeException.class)
    public void testInvokeFailWithQueryParamMessage() throws Exception
    {
        when(delegate.getItem(ID)).thenThrow(new RuntimeException());
        testApi.getItem(ID);
        verify(exceptionHandler).createRestApiException(any(InvocationTargetException.class), eq(", id=100500"));
    }

    @Test(expected = RestApiRuntimeException.class)
    public void testInvokeFailWithPathParamMessage() throws Exception
    {
        when(delegate.getItemStatus(ID)).thenThrow(new RuntimeException());
        testApi.getItemStatus(ID);
        verify(exceptionHandler).createRestApiException(any(InvocationTargetException.class), eq(", id=100500"));
    }

    @Test(expected = RestApiRuntimeException.class)
    public void testInvokeFailWithDtoParamMessage() throws Exception
    {
        when(delegate.updateItem(DTO)).thenThrow(new RuntimeException());
        testApi.updateItem(DTO);
        verify(exceptionHandler).createRestApiException(any(InvocationTargetException.class), eq(", id=100500"));
    }

    @DetailedErrorMessage(fields = "id")
    private interface TestApi
    {
        TestDto getItem(@QueryParam("id") String id);

        TestDto updateItem(TestDto dto);

        @Path("/{id}/status")
        String getItemStatus(@PathParam("id") String id);
    }

    @AllArgsConstructor
    private static class TestDto
    {
        private String id;
    }
}
