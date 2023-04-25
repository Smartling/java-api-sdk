package com.smartling.api.filetranslations.v2;

import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.ClientFactory;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.AuthorizationRequestFilter;

public class FileTranslationsApiFactory extends AbstractApiFactory<FileTranslationsApi>
{
    public FileTranslationsApiFactory()
    {
        super();
    }

    public FileTranslationsApiFactory(ClientFactory clientFactory)
    {
        super(clientFactory);
    }

    @Override
    protected Class<FileTranslationsApi> getApiClass()
    {
        return FileTranslationsApi.class;
    }

    @Override
    public FileTranslationsApi buildApi(final AuthorizationRequestFilter authFilter, ClientConfiguration config)
    {
        ClientConfiguration filesConfig = DefaultClientConfiguration.builder()
            .baseUrl(config.getBaseUrl())
            .clientRequestFilters(config.getClientRequestFilters())
            .clientResponseFilters(config.getClientResponseFilters())
            .libNameVersionHolder(config.getLibNameVersionHolder())
            .httpClientConfiguration(config.getHttpClientConfiguration())
            .resteasyProviderFactory(config.getResteasyProviderFactory())
            .build();

        return super.buildApi(authFilter, filesConfig);
    }
}
