package com.smartling.api.jobbatches.v1;

import com.smartling.api.jobbatches.util.FileUploadClientFactory;
import com.smartling.api.jobbatches.util.LibNameVersionHolder;
import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.ClientConfiguration;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.HttpClientConfiguration;
import com.smartling.api.v2.client.auth.AuthorizationRequestFilter;
import org.apache.http.HttpHeaders;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.util.ArrayList;
import java.util.List;

public class JobBatchesApiFactory extends AbstractApiFactory<JobBatchesApi>
{
    private static final int FILE_UPLOAD_SOCKET_TIMEOUT = 30_000;

    @Override
    protected Class<JobBatchesApi> getApiClass()
    {
        return JobBatchesApi.class;
    }

    @Override
    public JobBatchesApi buildApi(AuthorizationRequestFilter authFilter, ClientConfiguration config)
    {
        HttpClientConfiguration httpClientConfiguration = config.getHttpClientConfiguration();
        if (httpClientConfiguration.getSocketTimeout() == HttpClientConfiguration.DEFAULT_SOCKET_TIMEOUT)
        {
            httpClientConfiguration.setSocketTimeout(FILE_UPLOAD_SOCKET_TIMEOUT);
        }

        List<ClientRequestFilter> clientRequestFilters = new ArrayList<>(config.getClientRequestFilters().size() + 1);
        clientRequestFilters.add(userAgentFilter());

        ClientConfiguration jobsBatchConfig = DefaultClientConfiguration
            .builder()
            .baseUrl(config.getBaseUrl())
            .httpClientConfiguration(httpClientConfiguration)
            .clientRequestFilters(clientRequestFilters)
            .clientResponseFilters(config.getClientResponseFilters())
            .resteasyProviderFactory(config.getResteasyProviderFactory())
            .build();

        JobBatchesApi jobBatchesApi = super.buildApi(authFilter, jobsBatchConfig);

        List<ClientRequestFilter> fileUploadFilters = new ArrayList<>(clientRequestFilters.size() + 1);
        fileUploadFilters.add(authFilter);

        ResteasyWebTarget client = new FileUploadClientFactory().build(
            fileUploadFilters,
            config.getBaseUrl().toString(),
            httpClientConfiguration);

        return new FileUploadProxy(jobBatchesApi, client);
    }

    private ClientRequestFilter userAgentFilter()
    {
        return new ClientRequestFilter()
        {
            @Override
            public void filter(ClientRequestContext clientRequestContext)
            {
                try
                {
                    clientRequestContext.getHeaders().add(HttpHeaders.USER_AGENT,
                            LibNameVersionHolder.getClientLibName() + "/" + LibNameVersionHolder.getClientLibVersion());
                }
                catch (Exception ignored)
                {
                }
            }
        };
    }
}
