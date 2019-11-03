package com.smartling.api.v2;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Simple hostname verifier that insecurely trusts all host names.
 * Only used if TLS is disabled.
 *
 * @author Scott Rossillo
 */
final class InsecureHostnameVerifier implements HostnameVerifier
{
    @Override
    public boolean verify(String s, SSLSession sslSession)
    {
        return true;
    }
}
