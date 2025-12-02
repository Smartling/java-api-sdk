package com.smartling.api.v2.client;

import com.smartling.api.v2.client.exception.RestApiExceptionMapper;
import com.smartling.api.v2.client.useragent.LibNameVersionHolder;
import lombok.*;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Provides the default client configuration for Smartling API factories.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class DefaultClientConfiguration implements ClientConfiguration
{
    private static final String DEFAULT_API_HOST = "api.smartling.com";
    protected static final String DEFAULT_API_HOST_AND_PROTOCOL = "https://" + DEFAULT_API_HOST;
    protected static final URL DEFAULT_API_URL;

    static
    {
        try
        {
            DEFAULT_API_URL = new URL(DEFAULT_API_HOST_AND_PROTOCOL);
        }
        catch (MalformedURLException e)
        {
            throw new IllegalStateException(e);
        }
    }

    @Builder.Default
    @NonNull
    private URL baseUrl = DEFAULT_API_URL;

    @Builder.Default
    @NonNull
    private List<ClientRequestFilter> clientRequestFilters = Collections.emptyList();

    @Builder.Default
    @NonNull
    private List<ClientResponseFilter> clientResponseFilters = Collections.emptyList();

    @Builder.Default
    @NonNull
    private HttpClientConfiguration httpClientConfiguration = new HttpClientConfiguration();

    @Builder.Default
    private ResteasyProviderFactory resteasyProviderFactory = null;

    @Builder.Default
    private RestApiExceptionMapper exceptionMapper = null;

    @Builder.Default
    private LibNameVersionHolder libNameVersionHolder = null;
}
