package com.smartling.api.v2.jobbatches;

import com.smartling.api.v2.client.AbstractApiFactory;
import com.smartling.api.v2.client.HttpClientConfiguration;
import org.apache.http.HttpHeaders;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.util.ArrayList;
import java.util.List;

public class JobFacadeApiFactory extends AbstractApiFactory<JobFacadeApi>
{
    private static final int FILE_UPLOAD_SOCKET_TIMEOUT = 30_000;

    @Override
    protected Class<JobFacadeApi> getApiClass()
    {
        return JobFacadeApi.class;
    }

    @Override
    public JobFacadeApi buildApi(List<ClientRequestFilter> filterList, String hostAndProtocol, HttpClientConfiguration httpClientConfiguration)
    {
        if (httpClientConfiguration.getSocketTimeout() == HttpClientConfiguration.DEFAULT_SOCKET_TIMEOUT)
        {
            httpClientConfiguration.setSocketTimeout(FILE_UPLOAD_SOCKET_TIMEOUT);
        }

        List<ClientRequestFilter> clientRequestFilters = new ArrayList<>(filterList.size() + 1);
        clientRequestFilters.addAll(filterList);
//        clientRequestFilters.add(userAgentFilter());

        JobFacadeApi jobFacadeApi = super.buildApi(clientRequestFilters, hostAndProtocol, httpClientConfiguration);
        ResteasyWebTarget client = new FileUploadClientFactory().build(clientRequestFilters, hostAndProtocol, httpClientConfiguration);

        return new FileUploadProxy(jobFacadeApi, client);
    }

//    private ClientRequestFilter userAgentFilter()
//    {
//        return new ClientRequestFilter()
//        {
//            @Override
//            public void filter(ClientRequestContext clientRequestContext)
//            {
//                try
//                {
//                    clientRequestContext.getHeaders().add(HttpHeaders.USER_AGENT,
//                            LibNameVersionHolder.getClientLibName() + "/" + LibNameVersionHolder.getClientLibVersion());
//                }
//                catch (Exception ignored)
//                {
//                }
//            }
//        };
//    }
}
