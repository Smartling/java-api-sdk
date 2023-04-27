package com.smartling.api.glossary.v3.components.entry.auth;

import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.ClientFactory;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.AuthorizationRequestFilter;

/**
 * {@link EntryAuthorizeForTranslationApi} factory.
 */
public class EntryAuthorizeForTranslationApiFactory extends AbstractApiFactory<EntryAuthorizeForTranslationApi> {

    public EntryAuthorizeForTranslationApiFactory() {
        super();
    }

    public EntryAuthorizeForTranslationApiFactory(final ClientFactory clientFactory) {
        super(clientFactory);
    }

    @Override
    protected Class<EntryAuthorizeForTranslationApi> getApiClass() {
        return EntryAuthorizeForTranslationApi.class;
    }

    @Override
    public EntryAuthorizeForTranslationApi buildApi(final AuthorizationRequestFilter authFilter, final ClientConfiguration config) {
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
