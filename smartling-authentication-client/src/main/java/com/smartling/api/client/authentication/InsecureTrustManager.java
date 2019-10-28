package com.smartling.api.client.authentication;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * X509TrustManager which trusts any certificate.
 *
 * @author Scott Rossillo
 */
class InsecureTrustManager implements X509TrustManager
{
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
    {
        throw new UnsupportedOperationException("Unable to verify client certificates");
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
    {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new java.security.cert.X509Certificate[] {};
    }

    static SSLSocketFactory insecureSSLSocketFactory()
    {
        try
        {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new InsecureTrustManager()}, null);
            return sslContext.getSocketFactory();
        }
        catch (Exception ex)
        {
            throw new IllegalStateException("Unable to create inscure SSL socket factory", ex);
        }
    }
}
