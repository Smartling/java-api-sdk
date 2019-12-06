package com.smartling.api.v2.client.useragent;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.http.HttpHeaders;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;

@AllArgsConstructor
public class UserAgentFilter implements ClientRequestFilter
{
    @NonNull
    private LibNameVersionHolder libNameVersionHolder;

    @Override
    public void filter(ClientRequestContext requestContext)
    {
        try
        {
            requestContext.getHeaders().add(HttpHeaders.USER_AGENT,
                libNameVersionHolder.getClientLibName() + "/" + libNameVersionHolder.getClientLibVersion());
        }
        catch (Exception ignored)
        {
        }
    }
}
