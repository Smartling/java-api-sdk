package com.smartling.api.jobbatches.v2;

import com.smartling.api.jobbatches.util.FileUploadClientFactory;
import com.smartling.api.jobbatches.util.LibNameVersionHolder;
import com.smartling.api.v2.client.*;
import com.smartling.api.v2.client.auth.AuthorizationRequestFilter;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import java.util.ArrayList;
import java.util.List;

public class JobBatchesApiFactory extends AbstractApiFactory<JobBatchesApi>
{
    private static final int FILE_UPLOAD_SOCKET_TIMEOUT = 30_000;

    public  JobBatchesApiFactory()
    {
        super();
    }

    public JobBatchesApiFactory(ClientFactory clientFactory)
    {
        super(clientFactory);
    }

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
        clientRequestFilters.addAll(config.getClientRequestFilters());
        clientRequestFilters.add(userAgentFilter());

        ClientConfiguration jobsBatchConfig = DefaultClientConfiguration.builder()
            .baseUrl(config.getBaseUrl())
            .httpClientConfiguration(httpClientConfiguration)
            .clientRequestFilters(clientRequestFilters)
            .clientResponseFilters(config.getClientResponseFilters())
            .resteasyProviderFactory(config.getResteasyProviderFactory())
            .build();

        JobBatchesApi jobBatchesApi = super.buildApi(authFilter, config);

        List<ClientRequestFilter> fileUploadFilters = new ArrayList<>(clientRequestFilters.size() + 1);
        fileUploadFilters.add(authFilter);

        FileUploadClientFactory factory = new FileUploadClientFactory();
        ResteasyClient client = factory.build(httpClientConfiguration);
        ResteasyWebTarget target = factory.build(
            client,
            fileUploadFilters,
            jobsBatchConfig.getBaseUrl().toString());

        return new FileUploadProxy(jobBatchesApi, client, target);
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
