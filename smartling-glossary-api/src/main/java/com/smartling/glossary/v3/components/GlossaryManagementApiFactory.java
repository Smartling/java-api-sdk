package com.smartling.glossary.v3.components;

import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.ClientFactory;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.AuthorizationRequestFilter;
import com.smartling.glossary.v3.components.entry.EntryManagementApi;

/**
 * {@link GlossaryManagementApi} factory.
 */
public class GlossaryManagementApiFactory extends AbstractApiFactory<GlossaryManagementApi> {

    public GlossaryManagementApiFactory() {
        super();
    }

    public GlossaryManagementApiFactory(final ClientFactory clientFactory) {
        super(clientFactory);
    }

    @Override
    protected Class<GlossaryManagementApi> getApiClass() {
        return GlossaryManagementApi.class;
    }

    @Override
    public GlossaryManagementApi buildApi(final AuthorizationRequestFilter authFilter, final ClientConfiguration config) {
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
