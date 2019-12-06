package com.smartling.api.v2.client;

import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.v2.response.EmptyData;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.*;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class ClientFactoryTest
{
    private ClientFactory factory;
    private String tlsProperty;
    private List<ClientRequestFilter> requestFilters;
    private List<ClientResponseFilter> responseFilters;

    @Before
    public void setUp()
    {
        factory = new ClientFactory();

        final List<ClientRequestFilter> filters = new LinkedList<>();
        filters.add(new BearerAuthStaticTokenFilter("foo"));
        filters.add(new Bar());
        this.requestFilters = filters;

        this.responseFilters = new LinkedList<>();
    }

    @Test
    public void httpProxyTest() throws Exception
    {
        final MockWebServer mockProxyServer = new MockWebServer();
        try
        {
            mockProxyServer.start();

            String responseBody = "{\"response\":{\"data\":null}}";
            final MockResponse response = new MockResponse()
                    .setResponseCode(HttpStatus.SC_OK)
                    .setHeader(HttpHeaders.CONTENT_LENGTH, responseBody.length())
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                    .setBody(responseBody);
            mockProxyServer.enqueue(response);

            HttpClientConfiguration configuration = new HttpClientConfiguration();
            configuration.setProxyHost(mockProxyServer.getHostName());
            configuration.setProxyPort(mockProxyServer.getPort());
            final Foo foo = factory.build(requestFilters, Collections.<ClientResponseFilter>emptyList(), "http://localhost:9595/", Foo.class, configuration, null, null);
            foo.getFoo("");
        }
        finally
        {
            mockProxyServer.shutdown();
        }
    }

    @Test(expected = NullPointerException.class)
    public void testBuildNullRequestFilterList() throws Exception
    {
        factory.build(null, responseFilters, "foo", Foo.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildEmptyRequestFilterList() throws Exception
    {
        factory.build(new LinkedList<ClientRequestFilter>(),  responseFilters, "foo", Foo.class);
    }

    @Test(expected = NullPointerException.class)
    public void testBuildNullResponseFilterList() throws Exception
    {
        factory.build(requestFilters, null, "foo", Foo.class);
    }

    @Test(expected = NullPointerException.class)
    public void testBuildNullDomain() throws Exception
    {
        factory.build(requestFilters, responseFilters, null, Foo.class);
    }

    @Test(expected = NullPointerException.class)
    public void testBuildNullKlass() throws Exception
    {
        factory.build(requestFilters, responseFilters, "foo", null);
    }

    @Test(expected = NullPointerException.class)
    public void testBuildNullDeserializers() throws Exception
    {
        ClientFactory f = spy(factory);
        when(f.getDeserializerMap()).thenReturn(null);
        f.build(requestFilters, responseFilters, "foo", Foo.class, new HttpClientConfiguration(), null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testBuildNullHttpConfiguration() throws Exception
    {
        factory.build(requestFilters, responseFilters, "foo", Foo.class, null, null, null);
    }

    @Test
    public void testBuildList() throws Exception
    {
        final Foo foo = factory.build(requestFilters, responseFilters, "foo", Foo.class);
        assertNotNull(foo);
    }

    @Test
    public void testSocketTimeout() throws IOException, InterruptedException, TimeoutException, ExecutionException
    {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        final MockWebServer webServer = new MockWebServer();
        webServer.start();
        try
        {
            String responseBody = "{\"response\":{\"data\":null}}";
            final MockResponse response = new MockResponse()
                .setBodyDelay(2, TimeUnit.SECONDS)
                .setResponseCode(HttpStatus.SC_OK)
                .setHeader(HttpHeaders.CONTENT_LENGTH, responseBody.length())
                .setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)
                .setBody(responseBody);
            webServer.enqueue(response);

            HttpClientConfiguration httpClientConfiguration = new HttpClientConfiguration()
                .setSocketTimeout(10);
            final Foo foo = factory.build(requestFilters,
                Collections.<ClientResponseFilter>emptyList(),
                webServer.url("/").toString(),
                Foo.class,
                httpClientConfiguration,
                null,
                null);
            Future<EmptyData> future = executorService.submit(new Callable<EmptyData>()
            {
                @Override
                public EmptyData call()
                {
                    return foo.getFoo("uid");
                }
            });
            //we can't use org.junit.Test#timeout due to the long-running factory#build method
            future.get(1000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e)
        {
            Throwable exception = e;
            while (null != exception)
            {
                if (SocketTimeoutException.class.isAssignableFrom(exception.getClass()))
                    return;
                exception = exception.getCause();
            }
            throw e;
        } finally
        {
            executorService.shutdown();
            webServer.shutdown();
        }
    }

    @Path("/foo-api/v2")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public interface Foo
    {
        @GET
        @Path("/accounts/{accountUid}/foo")
        EmptyData getFoo(@PathParam("accountUid") String accountUid);
    }

    public class Bar implements ClientRequestFilter
    {
        @Override
        public void filter(final ClientRequestContext requestContext) throws IOException
        {

        }
    }
}
