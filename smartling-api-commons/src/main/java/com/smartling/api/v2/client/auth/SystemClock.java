package com.smartling.api.v2.client.auth;

class SystemClock implements Clock
{
    @Override
    public long currentTimeMillis()
    {
        return System.currentTimeMillis();
    }
}
