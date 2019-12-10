package com.smartling.api.v2.client.useragent;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LibNameVersionHolderTest
{
    @Test
    public void testCreateWithUserProvidedNameVersion() throws Exception
    {
        String name = "custom_name";
        String version = "custom_version";
        LibNameVersionHolder holder = new LibNameVersionHolder(name, version);

        assertEquals(name, holder.getClientLibName());
        assertEquals(version, holder.getClientLibVersion());
    }

    @Test
    public void testCreateWithClass() throws Exception
    {
        Class klazz = this.getClass();
        LibNameVersionHolder holder = new LibNameVersionHolder(klazz);

        assertEquals("test-api-java", holder.getClientLibName());
        assertEquals("42.13", holder.getClientLibVersion());
    }

    @Test(expected = LibNameVerionPropertiesReadError.class)
    public void testFailWithUnknownClass() throws Exception
    {
        Class klazz = Assert.class;
        new LibNameVersionHolder(klazz);
    }
}
