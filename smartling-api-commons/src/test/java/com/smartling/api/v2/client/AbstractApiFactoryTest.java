package com.smartling.api.v2.client;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.smartling.api.v2.client.auth.Authenticator;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;

import java.net.URL;
import java.util.*;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;

import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class AbstractApiFactoryTest
{
    private static final String USER_IDENTIFIER = "userIdentifier";
    private static final String USER_SECRET     = "userSecret";
    private static final String DEFAULT_DOMAIN  = "https://api.smartling.com";

    private Authenticator authenticator;
    private ClientFactory clientFactory;
    private FooFactory fooFactory;

    private Map<Class<?>, JsonDeserializer<?>> deserializerMap;

    @Before
    public void setUp()
    {
        deserializerMap = new HashMap<>();

        clientFactory = mock(ClientFactory.class);
        when(clientFactory.getDeserializerMap()).thenReturn(deserializerMap);

        authenticator = mock(Authenticator.class);

        fooFactory = spy(new FooFactory(clientFactory));

        final Foo foo = mock(Foo.class);

        when(clientFactory.build(any(List.class), any(List.class), any(String.class), eq(Foo.class), any(HttpClientConfiguration.class), any(ResteasyProviderFactory.class))).thenReturn(foo);
        when(clientFactory.build(ArgumentMatchers.<ClientRequestFilter>anyList(), ArgumentMatchers.<ClientResponseFilter>anyList(), anyString(), eq(Foo.class), any(HttpClientConfiguration.class),
            (ResteasyProviderFactory) isNull())).thenReturn(foo);
    }

    @Test(expected = NullPointerException.class)
    public void testBuildApiNullUserIdentifier() throws Exception
    {
        fooFactory.buildApi((String)null, USER_SECRET);
    }


    @Test(expected = NullPointerException.class)
    public void testBuildApiNullUserSecret() throws Exception
    {
        fooFactory.buildApi(USER_IDENTIFIER, null);
    }

    @Test(expected = NullPointerException.class)
    public void testBuildApiNullFilter() throws Exception
    {
        fooFactory.buildApi(null);
    }

    @Test
    public void testBuildApiUser() throws Exception
    {
        assertNotNull(fooFactory.buildApi(USER_IDENTIFIER, USER_SECRET));
        verify(clientFactory, times(1)).build(ArgumentMatchers.<ClientRequestFilter>anyList(), ArgumentMatchers.<ClientResponseFilter>anyList(), eq(DEFAULT_DOMAIN), eq(Foo.class), any(HttpClientConfiguration.class), eq((ResteasyProviderFactory)null));
    }

    @Test
    public void testBuildApiUserAuthFilter() throws Exception
    {
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter(USER_IDENTIFIER);
        final ClientConfiguration config = DefaultClientConfiguration.builder().baseUrl(new URL(DEFAULT_DOMAIN)).build();
        assertNotNull(fooFactory.buildApi(tokenFilter));
        verify(clientFactory, times(1)).build(ArgumentMatchers.<ClientRequestFilter>anyList(), ArgumentMatchers.<ClientResponseFilter>anyList(), anyString(), eq(Foo.class), any(HttpClientConfiguration.class), eq((ResteasyProviderFactory)null));
    }

    @Test
    public void testBuildApiUserAuthAndHost() throws Exception
    {
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter(USER_IDENTIFIER);
        final String domain = "http://foo.com";
        final ClientConfiguration config = DefaultClientConfiguration.builder()
            .baseUrl(new URL(domain))
            .build();

        assertNotNull(fooFactory.buildApi(tokenFilter, config));
        verify(clientFactory, times(1)).build(ArgumentMatchers.<ClientRequestFilter>anyList(), ArgumentMatchers.<ClientResponseFilter>anyList(), eq(domain), eq(Foo.class), any(HttpClientConfiguration.class), eq((ResteasyProviderFactory)null));
        verify(clientFactory, times(1)).build(ArgumentMatchers.<ClientRequestFilter>anyList(), ArgumentMatchers.<ClientResponseFilter>anyList(), eq(domain), eq(Foo.class), any(HttpClientConfiguration.class), eq((ResteasyProviderFactory)null));
    }

    @Test
    public void testBuildApiUserProviderFactory() throws Exception
    {
        final List<ClientRequestFilter> requestFilters = new LinkedList<>();
        final String domain = "http://foo.com";

        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter(USER_IDENTIFIER);
        ResteasyProviderFactory resteasyProviderFactory = new ResteasyProviderFactory();
        final ClientConfiguration config = DefaultClientConfiguration.builder()
                                                                     .baseUrl(new URL(domain))
                                                                     .resteasyProviderFactory(resteasyProviderFactory)
                                                                     .build();
        requestFilters.add(tokenFilter);
        assertNotNull(fooFactory.buildApi(tokenFilter, config));
        verify(clientFactory, times(1)).build(eq(requestFilters), ArgumentMatchers.<ClientResponseFilter>anyList(), eq(domain), eq(Foo.class), any(HttpClientConfiguration.class), eq(resteasyProviderFactory));
    }

    @Test
    public void testBuildApiUserHttpClientConfigurationAndProviderFactory() throws Exception
    {
        BearerAuthStaticTokenFilter authFilter = new BearerAuthStaticTokenFilter(USER_IDENTIFIER);
        final String domain = "http://foo.com";

        HttpClientConfiguration httpClientConfiguration = new HttpClientConfiguration();
        ResteasyProviderFactory resteasyProviderFactory = new ResteasyProviderFactory();
        ClientConfiguration config = DefaultClientConfiguration
            .builder()
            .baseUrl(new URL(domain))
            .httpClientConfiguration(httpClientConfiguration)
            .resteasyProviderFactory(resteasyProviderFactory)
            .build();

        assertNotNull(fooFactory.buildApi(authFilter, config));
        verify(clientFactory, times(1)).build(ArgumentMatchers.<ClientRequestFilter>anyList(),  ArgumentMatchers.<ClientResponseFilter>anyList(), eq(domain), eq(Foo.class), eq(httpClientConfiguration), eq(resteasyProviderFactory));
    }

    /*
    @Test
    public void testGetHttpClientConfiguration() throws Exception
    {
        final HttpClientConfiguration configuration = fooFactory.getHttpClientConfiguration();
        assertNotNull(configuration);
        assertEquals(60000, configuration.getConnectionRequestTimeout());
        assertEquals(10000, configuration.getConnectionTimeout());
        assertEquals(20, configuration.getMaxThreadPerRoute());
        assertEquals(20, configuration.getMaxThreadTotal());
        assertEquals(10000, configuration.getSocketTimeout());
        assertTrue(configuration.isStaleConnectionCheckEnabled());
    }

    @Test
    public void test()
    {
        assertEquals(Collections.emptyList(), fooFactory.getClientResponseFilters());
    }
     */

    public interface Foo {}

    public class FooFactory extends AbstractApiFactory<Foo>
    {
        public FooFactory(final ClientFactory clientFactory)
        {
            super(clientFactory);
        }

        @Override
        protected Class<Foo> getApiClass()
        {
            return Foo.class;
        }
    }

}
