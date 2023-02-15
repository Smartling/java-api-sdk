package com.smartling.api.v2.client.graphql;

import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.v2.response.EmptyData;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
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
import java.util.LinkedList;
import java.util.List;

public class GraphQlClientFactoryTest
{
    private GraphQlClientFactory graphQlClientFactory;
    private List<ClientRequestFilter> requestFilters;
    private List<ClientResponseFilter> responseFilters;

    @Before
    public void setUp()
    {
        graphQlClientFactory = new GraphQlClientFactory();

        final List<ClientRequestFilter> filters = new LinkedList<>();
        filters.add(new BearerAuthStaticTokenFilter("foo"));
        filters.add(new Bar());
        this.requestFilters = filters;

        this.responseFilters = new LinkedList<>();
    }

    @Test
    public void graphQlClientFactoryTest() throws IOException
    {
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

            final Foo foo = graphQlClientFactory.build(requestFilters, responseFilters, webServer.url("/").toString(), Foo.class);
            EmptyData data = foo.getFoo("uid");
        }
        finally
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
