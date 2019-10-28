package com.smartling.api.external.client;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.smartling.api.external.client.exception.RestApiRuntimeException;
import com.smartling.api.external.client.graphql.GraphQlClientFactory;
import com.smartling.web.api.v2.EmptyData;
import okhttp3.internal.SslContextBuilder;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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
        tlsProperty = System.getProperty(ClientFactory.TLS_PROPERTY);

        final List<ClientRequestFilter> filters = new LinkedList<>();
        filters.add(new Bar());
        this.requestFilters = filters;

        this.responseFilters = new LinkedList<>();
    }

    @After
    public void tearDown()
    {

        if (tlsProperty == null)
            System.clearProperty(ClientFactory.TLS_PROPERTY);
        else
            System.setProperty(ClientFactory.TLS_PROPERTY, tlsProperty);
    }

    private void sslTest(final boolean enableSelfSignedCertificates) throws Exception
    {
        final String body = "{\"response\":{\"data\":null}}";

        final MockWebServer mockWebServer = new MockWebServer();
        try
        {
            mockWebServer.useHttps(SslContextBuilder.localhost().getSocketFactory(), false);
            mockWebServer.start();

            final MockResponse response = new MockResponse()
                    .setResponseCode(HttpStatus.SC_OK)
                    .setHeader(HttpHeaders.CONTENT_LENGTH, body.length())
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                    .setBody(body);

            mockWebServer.enqueue(response);

            System.setProperty(ClientFactory.TLS_PROPERTY, String.valueOf(enableSelfSignedCertificates));

            final Foo foo = factory.build(requestFilters, responseFilters, mockWebServer.url("/").toString(), Foo.class, factory.getDefaultDeserializerMap(), new HttpClientConfiguration(), null);
            foo.getFoo("");
        }
        finally
        {
            mockWebServer.shutdown();
        }
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
            final Foo foo = factory.build(requestFilters, Collections.<ClientResponseFilter>emptyList(), "http://localhost:9595/", Foo.class, factory.getDefaultDeserializerMap(), configuration, null);
            foo.getFoo("");
        }
        finally
        {
            mockProxyServer.shutdown();
        }
    }

    @Test
    public void testEnableSslSecurityFalse() throws Exception
    {
        System.setProperty(ClientFactory.TLS_PROPERTY, "false");
        assertFalse(factory.enableSslSecurity());
    }

    @Test
    public void testEnableSslSecurity() throws Exception
    {
        System.setProperty(ClientFactory.TLS_PROPERTY, "true");

        boolean enabled = true;
        if (System.getenv(ClientFactory.TLS_ENVIRONMENT_VAR) != null)
            enabled = Boolean.valueOf(System.getenv(ClientFactory.TLS_ENVIRONMENT_VAR));

        assertEquals(enabled, factory.enableSslSecurity());
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
        factory.build(requestFilters, responseFilters, "foo", Foo.class, null, new HttpClientConfiguration(), null);
    }

    @Test(expected = NullPointerException.class)
    public void testBuildNullHttpConfiguration() throws Exception
    {
        factory.build(requestFilters, responseFilters, "foo", Foo.class, new HashMap<Class<?>, JsonDeserializer<?>>(), null, null);
    }

    @Test
    public void testBuildList() throws Exception
    {
        final Foo foo = factory.build(requestFilters, responseFilters, "foo", Foo.class);
        assertNotNull(foo);
    }

    @Test(expected = RestApiRuntimeException.class)
    public void testSelfSignedCertificateFail() throws Exception
    {
        sslTest(true);
    }

    @Test
    public void testSelfSignedCertificatePass() throws Exception
    {
        sslTest(false);
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
                factory.getDefaultDeserializerMap(),
                httpClientConfiguration,
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

    @Test
    public void testGraphQlClientFactory() throws IOException
    {
        final GraphQlClientFactory graphQlClientFactory = new GraphQlClientFactory();
        final MockWebServer webServer = new MockWebServer();
        webServer.start();
        try
        {
            String responseBody = "{\"data\":{}}";
            final MockResponse response = new MockResponse()
                .setResponseCode(HttpStatus.SC_OK)
                .setHeader(HttpHeaders.CONTENT_LENGTH, responseBody.length())
                .setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)
                .setBody(responseBody);
            webServer.enqueue(response);

            final Foo foo = graphQlClientFactory.build(requestFilters,
                Collections.<ClientResponseFilter>emptyList(),
                webServer.url("/").toString(),
                Foo.class,
                graphQlClientFactory.getDefaultDeserializerMap(),
                new HttpClientConfiguration(),
                null);
            foo.getFoo("uid");
        } finally
        {
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