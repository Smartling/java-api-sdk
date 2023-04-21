package com.smartling.glossary.v3.components.ie;

import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.ClientFactory;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.AuthorizationRequestFilter;
import com.smartling.glossary.v3.GlossaryApi;

/**
 * {@link GlossaryApi} factory.
 */
public class ImportExportApiFactory extends AbstractApiFactory<ImportExportApi> {

    public ImportExportApiFactory() {
        super();
    }

    public ImportExportApiFactory(final ClientFactory clientFactory) {
        super(clientFactory);
    }

    @Override
    protected Class<ImportExportApi> getApiClass() {
        return ImportExportApi.class;
    }

    @Override
    public ImportExportApi buildApi(final AuthorizationRequestFilter authFilter, final ClientConfiguration config) {
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
