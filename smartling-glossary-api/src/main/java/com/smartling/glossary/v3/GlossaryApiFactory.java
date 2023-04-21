package com.smartling.glossary.v3;

import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.ClientFactory;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.AuthorizationRequestFilter;

/**
 * {@link GlossaryApi} factory.
 */
public class GlossaryApiFactory extends AbstractApiFactory<GlossaryApi> {
    public GlossaryApiFactory() {
        super();
    }

    public GlossaryApiFactory(final ClientFactory clientFactory) {
        super(clientFactory);
    }

    @Override
    protected Class<GlossaryApi> getApiClass() {
        return GlossaryApi.class;
    }

    @Override
    public GlossaryApi buildApi(final AuthorizationRequestFilter authFilter, final ClientConfiguration config) {
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
