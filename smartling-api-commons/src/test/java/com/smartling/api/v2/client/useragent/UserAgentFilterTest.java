package com.smartling.api.v2.client.useragent;

import org.junit.Test;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserAgentFilterTest
{
    @Test
    public void testSetUserAgentHeader() throws Exception
    {
        final LibNameVersionHolder libNameVersionHolder = new LibNameVersionHolder("test-name", "test.version");
        final UserAgentFilter filter = new UserAgentFilter(libNameVersionHolder);
        final ClientRequestContext requestContext = mock(ClientRequestContext.class);
        when(requestContext.getHeaders()).thenReturn(mock(MultivaluedMap.class));

        filter.filter(requestContext);

        verify(requestContext.getHeaders(), times(1)).add(eq(HttpHeaders.USER_AGENT), eq("test-name/test.version"));
    }
}
