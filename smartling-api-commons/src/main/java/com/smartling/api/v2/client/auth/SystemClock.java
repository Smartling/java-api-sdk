package com.smartling.api.v2.client.auth;

public class SystemClock implements Clock
{
    @Override
    public long currentTimeMillis()
    {
        return System.currentTimeMillis();
    }
}
