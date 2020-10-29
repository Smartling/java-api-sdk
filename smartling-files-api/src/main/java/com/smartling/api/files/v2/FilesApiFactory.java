package com.smartling.api.files.v2;

import com.smartling.api.files.v2.exceptions.FilesApiExceptionMapper;
import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.ClientFactory;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.AuthorizationRequestFilter;

public class FilesApiFactory extends AbstractApiFactory<FilesApi>
{
    public  FilesApiFactory()
    {
        super();
    }

    public FilesApiFactory(ClientFactory clientFactory)
    {
        super(clientFactory);
    }

    @Override
    protected Class<FilesApi> getApiClass()
    {
        return FilesApi.class;
    }

    @Override
    public FilesApi buildApi(final AuthorizationRequestFilter authFilter, ClientConfiguration config)
    {
        ClientConfiguration filesConfig = DefaultClientConfiguration.builder()
            .baseUrl(config.getBaseUrl())
            .clientRequestFilters(config.getClientRequestFilters())
            .clientResponseFilters(config.getClientResponseFilters())
            .libNameVersionHolder(config.getLibNameVersionHolder())
            .httpClientConfiguration(config.getHttpClientConfiguration())
            .resteasyProviderFactory(config.getResteasyProviderFactory())
            .exceptionMapper(new FilesApiExceptionMapper())
            .build();

        return super.buildApi(authFilter, filesConfig);
    }
}
