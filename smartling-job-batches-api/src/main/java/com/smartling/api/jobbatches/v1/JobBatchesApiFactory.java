package com.smartling.api.jobbatches.v1;

import com.smartling.api.jobbatches.util.FileUploadClientFactory;
import com.smartling.api.jobbatches.util.LibNameVersionHolder;
import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.HttpClientConfiguration;
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
    public JobBatchesApi buildApi(List<ClientRequestFilter> filterList, String hostAndProtocol, HttpClientConfiguration httpClientConfiguration)
    {
        if (httpClientConfiguration.getSocketTimeout() == HttpClientConfiguration.DEFAULT_SOCKET_TIMEOUT)
        {
            httpClientConfiguration.setSocketTimeout(FILE_UPLOAD_SOCKET_TIMEOUT);
        }

        List<ClientRequestFilter> clientRequestFilters = new ArrayList<>(filterList.size() + 1);
        clientRequestFilters.addAll(filterList);
        clientRequestFilters.add(userAgentFilter());

        JobBatchesApi jobBatchesApi = super.buildApi(clientRequestFilters, hostAndProtocol, httpClientConfiguration);
        ResteasyWebTarget client = new FileUploadClientFactory().build(clientRequestFilters, hostAndProtocol, httpClientConfiguration);

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
