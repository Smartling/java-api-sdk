package com.smartling.api.files.v2;

import com.smartling.api.files.v2.exceptions.FilesApiExceptionMapper;
import com.smartling.api.files.v2.resteasy.ext.TranslatedFileMultipartReader;
import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.ClientFactory;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.AuthorizationRequestFilter;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

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
        ResteasyProviderFactory resteasyProviderFactory = config.getResteasyProviderFactory();
        if (null == resteasyProviderFactory)
            resteasyProviderFactory = new ResteasyClientBuilderImpl().getProviderFactory();
        resteasyProviderFactory.register(TranslatedFileMultipartReader.class);

        ClientConfiguration filesConfig = DefaultClientConfiguration.builder()
            .baseUrl(config.getBaseUrl())
            .clientRequestFilters(config.getClientRequestFilters())
            .clientResponseFilters(config.getClientResponseFilters())
            .libNameVersionHolder(config.getLibNameVersionHolder())
            .httpClientConfiguration(config.getHttpClientConfiguration())
            .resteasyProviderFactory(resteasyProviderFactory)
            .exceptionMapper(new FilesApiExceptionMapper())
            .build();

        return super.buildApi(authFilter, filesConfig);
    }
}
