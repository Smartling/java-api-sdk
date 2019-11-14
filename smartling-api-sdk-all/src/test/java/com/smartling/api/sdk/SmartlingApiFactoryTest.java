package com.smartling.api.sdk;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SmartlingApiFactoryTest
{
    private SmartlingApiFactory factory;

    @Before
    public void setUp() throws Exception
    {
        factory = new SmartlingApiFactory();
    }

    @Test
    public void build()
    {
        SmartlingApi api = new SmartlingApiFactory().build("userId", "secret");
        assertNotNull(api);
    }

    @Test(expected = NullPointerException.class)
    public void buildNullUserIdentifier()
    {
        factory.build(null, "secret");
    }

    @Test(expected = NullPointerException.class)
    public void buildNullUserSecret()
    {
        factory.build("userId", null);
    }
}
