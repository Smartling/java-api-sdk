package com.smartling.api.glossary.v3.components.entry;

import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.ClientFactory;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.AuthorizationRequestFilter;

/**
 * {@link EntryManagementApi} factory.
 */
public class EntryManagementApiFactory extends AbstractApiFactory<EntryManagementApi> {

    public EntryManagementApiFactory() {
        super();
    }

    public EntryManagementApiFactory(final ClientFactory clientFactory) {
        super(clientFactory);
    }

    @Override
    protected Class<EntryManagementApi> getApiClass() {
        return EntryManagementApi.class;
    }

    @Override
    public EntryManagementApi buildApi(final AuthorizationRequestFilter authFilter, final ClientConfiguration config) {
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
