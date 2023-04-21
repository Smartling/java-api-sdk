package com.smartling.glossary.v3.components.label;

import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.ClientFactory;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.auth.AuthorizationRequestFilter;
import com.smartling.api.v2.response.EmptyData;
import com.smartling.api.v2.response.ListResponse;
import com.smartling.glossary.v3.components.ie.ImportExportApi;
import com.smartling.glossary.v3.pto.label.GlossaryLabelCommandPTO;
import com.smartling.glossary.v3.pto.label.GlossaryLabelPTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.smartling.glossary.v3.components.Common.ACCOUNT_UID;
import static com.smartling.glossary.v3.components.Common.LABEL_UID;

/**
 * {@link LabelManagementApi} factory.
 */
public class LabelManagementApiFactory extends AbstractApiFactory<LabelManagementApi> {

    public LabelManagementApiFactory() {
        super();
    }

    public LabelManagementApiFactory(final ClientFactory clientFactory) {
        super(clientFactory);
    }

    @Override
    protected Class<LabelManagementApi> getApiClass() {
        return LabelManagementApi.class;
    }

    @Override
    public LabelManagementApi buildApi(final AuthorizationRequestFilter authFilter, final ClientConfiguration config) {
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
