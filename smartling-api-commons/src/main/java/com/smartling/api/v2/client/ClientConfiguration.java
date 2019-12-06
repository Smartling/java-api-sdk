package com.smartling.api.v2.client;

import com.smartling.api.v2.client.exception.RestApiExceptionMapper;
import com.smartling.api.v2.client.useragent.LibNameVersionHolder;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;
import java.net.URL;
import java.util.List;

/**
 * Provides a client configuration used to build APIs by API factories.
 */
public interface ClientConfiguration
{
    /**
     * Returns the base API URL.
     *
     * @return the base API <code>URL</code>
     */
    URL getBaseUrl();

    /**
     * Returns a list of client request filters to intercept API requests.
     *
     * @return a <code>List</code> of <code>ClientRequestFilter</code>s
     */
    List<ClientRequestFilter> getClientRequestFilters();

    /**
     * Returns a list of client response filters to intercept API responses.
     *
     * @return a <code>List</code> of <code>ClientResponseFilter</code>s
     */
    List<ClientResponseFilter> getClientResponseFilters();

    /**
     * Returns the HTTP client configuration for API requests.
     *
     * @return the {@link HttpClientConfiguration} for API requests
     */
    HttpClientConfiguration getHttpClientConfiguration();

    /**
     * Returns the Resteasy provider factory for API requests.
     *
     * @return the {@link ResteasyProviderFactory} for API requests
     */
    ResteasyProviderFactory getResteasyProviderFactory();

    /**
     * Returns the Exception mapper for custom API errors.
     *
     * @return the {@link RestApiExceptionMapper} for API requests
     */
    RestApiExceptionMapper getExceptionMapper();

    /**
     * Holds library name and version for user-agent header.
     *
     * @return the {@link LibNameVersionHolder} for API requests
     */
    LibNameVersionHolder getLibNameVersionHolder();
}
