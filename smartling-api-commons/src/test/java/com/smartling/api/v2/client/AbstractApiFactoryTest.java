package com.smartling.api.v2.client;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.smartling.api.v2.authentication.AuthenticationApi;
import com.smartling.api.v2.client.auth.BearerAuthStaticTokenFilter;
import com.smartling.api.v2.client.exception.RestApiExceptionMapper;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class AbstractApiFactoryTest
{
    private static final String USER_IDENTIFIER = "userIdentifier";
    private static final String USER_SECRET     = "userSecret";
    private static final String DEFAULT_DOMAIN  = "https://api.smartling.com";

    private ClientFactory clientFactory;
    private FooFactory fooFactory;

    @Captor
    private ArgumentCaptor<List<ClientRequestFilter>> requestFiltersCaptor;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);

        clientFactory = mock(ClientFactory.class);
        when(clientFactory.getDeserializerMap()).thenReturn(new HashMap<Class<?>, JsonDeserializer<?>>());

        fooFactory = spy(new FooFactory(clientFactory));

        final Foo foo = mock(Foo.class);

        when(clientFactory.build(
            ArgumentMatchers.<ClientRequestFilter>anyList(),
            ArgumentMatchers.<ClientResponseFilter>anyList(),
            anyString(), eq(AuthenticationApi.class),
            any(HttpClientConfiguration.class),
            (ResteasyProviderFactory) isNull(),
            (RestApiExceptionMapper) any())
        ).thenReturn(mock(AuthenticationApi.class));

        when(clientFactory.build(any(List.class),
            any(List.class),
            any(String.class),
            eq(Foo.class),
            any(HttpClientConfiguration.class),
            any(ResteasyProviderFactory.class),
            (RestApiExceptionMapper)any())
        ).thenReturn(foo);

        when(clientFactory.build(ArgumentMatchers.<ClientRequestFilter>anyList(),
            ArgumentMatchers.<ClientResponseFilter>anyList(),
            anyString(),
            eq(Foo.class),
            any(HttpClientConfiguration.class),
            (ResteasyProviderFactory) isNull(),
            (RestApiExceptionMapper)any())
        ).thenReturn(foo);
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
        verify(clientFactory, times(1)).build(ArgumentMatchers.<ClientRequestFilter>anyList(), ArgumentMatchers.<ClientResponseFilter>anyList(), eq(DEFAULT_DOMAIN), eq(Foo.class), any(HttpClientConfiguration.class), eq((ResteasyProviderFactory)null), eq((RestApiExceptionMapper)null));
    }

    @Test
    public void testBuildApiUserAuthFilter() throws Exception
    {
        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter(USER_IDENTIFIER);
        final ClientConfiguration config = DefaultClientConfiguration.builder().baseUrl(new URL(DEFAULT_DOMAIN)).build();
        assertNotNull(fooFactory.buildApi(tokenFilter));
        verify(clientFactory, times(1)).build(ArgumentMatchers.<ClientRequestFilter>anyList(), ArgumentMatchers.<ClientResponseFilter>anyList(), anyString(), eq(Foo.class), any(HttpClientConfiguration.class), eq((ResteasyProviderFactory)null), eq((RestApiExceptionMapper)null));
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
        verify(clientFactory, times(1)).build(ArgumentMatchers.<ClientRequestFilter>anyList(), ArgumentMatchers.<ClientResponseFilter>anyList(), eq(domain), eq(Foo.class), any(HttpClientConfiguration.class), eq((ResteasyProviderFactory)null), eq((RestApiExceptionMapper)null));
        verify(clientFactory, times(1)).build(ArgumentMatchers.<ClientRequestFilter>anyList(), ArgumentMatchers.<ClientResponseFilter>anyList(), eq(domain), eq(Foo.class), any(HttpClientConfiguration.class), eq((ResteasyProviderFactory)null), eq((RestApiExceptionMapper)null));
    }

    @Test
    public void testBuildApiUserProviderFactory() throws Exception
    {
        final String domain = "http://foo.com";

        final BearerAuthStaticTokenFilter tokenFilter = new BearerAuthStaticTokenFilter(USER_IDENTIFIER);
        ResteasyProviderFactory resteasyProviderFactory = new ResteasyProviderFactory();
        final ClientConfiguration config = DefaultClientConfiguration.builder()
                                                                     .baseUrl(new URL(domain))
                                                                     .resteasyProviderFactory(resteasyProviderFactory)
                                                                     .build();
        assertNotNull(fooFactory.buildApi(tokenFilter, config));
        verify(clientFactory, times(1)).build(requestFiltersCaptor.capture(), ArgumentMatchers.<ClientResponseFilter>anyList(), eq(domain), eq(Foo.class), any(HttpClientConfiguration.class), eq(resteasyProviderFactory), eq((RestApiExceptionMapper)null));
        assertTrue(requestFiltersCaptor.getValue().contains(tokenFilter));
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
        verify(clientFactory, times(1)).build(ArgumentMatchers.<ClientRequestFilter>anyList(),  ArgumentMatchers.<ClientResponseFilter>anyList(), eq(domain), eq(Foo.class), eq(httpClientConfiguration), eq(resteasyProviderFactory), eq((RestApiExceptionMapper)null));
    }

    @Test
    public void testBuildApiUserExceptionMapper() throws Exception
    {
        BearerAuthStaticTokenFilter authFilter = new BearerAuthStaticTokenFilter(USER_IDENTIFIER);
        final String domain = "http://foo.com";

        RestApiExceptionMapper exceptioMapper = mock(RestApiExceptionMapper.class);

        ClientConfiguration config = DefaultClientConfiguration
            .builder()
            .baseUrl(new URL(domain))
            .exceptionMapper(exceptioMapper)
            .build();

        assertNotNull(fooFactory.buildApi(authFilter, config));
        verify(clientFactory, times(1)).build(ArgumentMatchers.<ClientRequestFilter>anyList(),  ArgumentMatchers.<ClientResponseFilter>anyList(), eq(domain), eq(Foo.class), any(HttpClientConfiguration.class), eq((ResteasyProviderFactory)null), eq(exceptioMapper));
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
