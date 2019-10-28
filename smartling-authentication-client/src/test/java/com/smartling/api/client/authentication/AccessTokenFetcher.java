package com.smartling.api.client.authentication;

import java.util.concurrent.Callable;

public class AccessTokenFetcher implements Callable<String>
{
    private Authenticator authenticator;
    public AccessTokenFetcher(Authenticator authenticator)
    {
        this.authenticator = authenticator;
    }

    @Override
    public String call() throws Exception
    {
        return authenticator.getAccessToken();
    }
}
