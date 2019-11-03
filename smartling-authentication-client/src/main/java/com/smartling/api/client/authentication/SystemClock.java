package com.smartling.api.client.authentication;

public class SystemClock implements Clock
{
    @Override
    public long currentTimeMillis()
    {
        return System.currentTimeMillis();
    }
}
