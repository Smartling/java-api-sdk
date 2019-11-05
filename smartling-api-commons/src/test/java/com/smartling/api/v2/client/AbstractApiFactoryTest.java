package com.smartling.api.v2.client;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.smartling.api.v2.client.auth.BearerAuthSecretFilter;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;

import java.util.*;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;

import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.Before;
import org.junit.Ignore;
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

    private ClientFactory clientFactory;
    private FooFactory fooFactory;

    private Map<Class<?>, JsonDeserializer<?>> deserializerMap;

    @Before
    public void setUp()
    {
        deserializerMap = new HashMap<>();

        clientFactory = mock(ClientFactory.class);
        when(clientFactory.getDefaultDeserializerMap()).thenReturn(deserializerMap);

        fooFactory = new FooFactory(clientFactory);

        final Foo foo = mock(Foo.class);

        when(clientFactory.build(any(List.class), any(List.class), any(String.class), eq(Foo.class), eq(deserializerMap), any(HttpClientConfiguration.class), any(ResteasyProviderFactory.class))).thenReturn(foo);
        when(clientFactory.build(ArgumentMatchers.<ClientRequestFilter>anyList(), ArgumentMatchers.<ClientResponseFilter>anyList(), anyString(), eq(Foo.class), eq(deserializerMap), any(HttpClientConfiguration.class),
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

    @Test(expected = NullPointerException.class)
    public void testBuildApiNullProtocolAndHost() throws Exception
    {
        fooFactory.buildApi(new BearerAuthSecretFilter(USER_IDENTIFIER, USER_SECRET), null);
    }

    @Test(expected = NullPointerException.class)
    public void testBuildApiNullList() throws Exception
    {
        fooFactory.buildApi((List<ClientRequestFilter>)null, DEFAULT_DOMAIN);
    }

    @Test
    public void testBuildApiUser() throws Exception
    {
        assertNotNull(fooFactory.buildApi(USER_IDENTIFIER, USER_SECRET));
        verify(clientFactory, times(1)).build(ArgumentMatchers.<ClientRequestFilter>anyList(), ArgumentMatchers.<ClientResponseFilter>anyList(), eq(DEFAULT_DOMAIN), eq(Foo.class), eq(deserializerMap), any(HttpClientConfiguration.class), eq((ResteasyProviderFactory)null));
    }

    @Test
    public void testBuildApiUserAuthFilter() throws Exception
    {
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter(USER_IDENTIFIER);
        assertNotNull(fooFactory.buildApi(tokenFilter));
        verify(clientFactory, times(1)).build(ArgumentMatchers.<ClientRequestFilter>anyList(), ArgumentMatchers.<ClientResponseFilter>anyList(), eq(DEFAULT_DOMAIN), eq(Foo.class), eq(deserializerMap), any(HttpClientConfiguration.class), eq((ResteasyProviderFactory)null));
    }

    @Test
    public void testBuildApiUserAuthAndHost() throws Exception
    {
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter(USER_IDENTIFIER);
        final String domain = "http://foo.com";

        assertNotNull(fooFactory.buildApi(tokenFilter, domain));
        verify(clientFactory, times(1)).build(ArgumentMatchers.<ClientRequestFilter>anyList(), ArgumentMatchers.<ClientResponseFilter>anyList(), eq(domain), eq(Foo.class), eq(deserializerMap), any(HttpClientConfiguration.class), eq((ResteasyProviderFactory)null));
        verify(clientFactory, times(1)).build(ArgumentMatchers.<ClientRequestFilter>anyList(), ArgumentMatchers.<ClientResponseFilter>anyList(), eq(domain), eq(Foo.class), eq(deserializerMap), any(HttpClientConfiguration.class), eq((ResteasyProviderFactory)null));
    }

    @Test
    public void testBuildApiUserProviderFactory() throws Exception
    {
        final List<ClientRequestFilter> requestFilters = new LinkedList<>();
        requestFilters.add(new BearerAuthStaticTokenFilter(USER_IDENTIFIER));

        final String domain = "http://foo.com";

        ResteasyProviderFactory resteasyProviderFactory = new ResteasyProviderFactory();

        assertNotNull(fooFactory.buildApi(requestFilters, domain, resteasyProviderFactory));
        verify(clientFactory, times(1)).build(eq(requestFilters), ArgumentMatchers.<ClientResponseFilter>anyList(), eq(domain), eq(Foo.class), eq(deserializerMap), any(HttpClientConfiguration.class), eq(resteasyProviderFactory));
    }

    @Test
    public void testBuildApiUserHttpClientConfigurationAndProviderFactory() throws Exception
    {
        final List<ClientRequestFilter> requestFilters = new LinkedList<>();
        requestFilters.add(new BearerAuthStaticTokenFilter(USER_IDENTIFIER));

        final String domain = "http://foo.com";

        HttpClientConfiguration httpClientConfiguration = new HttpClientConfiguration();
        ResteasyProviderFactory resteasyProviderFactory = new ResteasyProviderFactory();

        assertNotNull(fooFactory.buildApi(requestFilters, domain, httpClientConfiguration, resteasyProviderFactory));
        verify(clientFactory, times(1)).build(eq(requestFilters),  ArgumentMatchers.<ClientResponseFilter>anyList(), eq(domain), eq(Foo.class), eq(deserializerMap), eq(httpClientConfiguration), eq(resteasyProviderFactory));
    }

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
