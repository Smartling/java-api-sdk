package com.smartling.api.v2.client;

import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultClientConfigurationTest
{
    @Test
    public void getBaseUrl()
    {
        assertNotNull(DefaultClientConfiguration.builder().build().getBaseUrl());
    }

    @Test(expected = NullPointerException.class)
    public void getBaseUrlNotNullable()
    {
        DefaultClientConfiguration.builder().baseUrl(null).build();
    }

    @Test
    public void getClientRequestFilters()
    {
        assertNotNull(DefaultClientConfiguration.builder().build().getClientRequestFilters());
    }

    @Test(expected = NullPointerException.class)
    public void getClientRequestFiltersNotNullable()
    {
       DefaultClientConfiguration.builder().clientRequestFilters(null).build();
    }

    @Test
    public void getClientResponseFilters()
    {
        assertNotNull(DefaultClientConfiguration.builder().build().getClientResponseFilters());
    }

    @Test(expected = NullPointerException.class)
    public void getClientResponseFiltersNotNullable()
    {
        DefaultClientConfiguration.builder().clientResponseFilters(null).build();
    }

    @Test
    public void getHttpClientConfiguration()
    {
        assertNotNull((DefaultClientConfiguration.builder().build().getHttpClientConfiguration()));
    }

    @Test(expected = NullPointerException.class)
    public void getHttpClientConfigurationNotNullable()
    {
        DefaultClientConfiguration.builder().httpClientConfiguration(null).build();
    }

    @Test
    public void getResteasyProviderFactory()
    {
        assertNull(DefaultClientConfiguration.builder().build().getResteasyProviderFactory());
    }
}
