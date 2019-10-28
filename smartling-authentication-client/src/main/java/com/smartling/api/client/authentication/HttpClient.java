package com.smartling.api.client.authentication;

import biz.source_code.base64Coder.Base64Coder;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Provides a tiny http client.
 *
 * @author Scott Rossillo
 */
class HttpClient
{
    private static final String TLS_ENVIRONMENT_VAR = "SMARTLING_TLS_ENABLED";
    private static final String TLS_PROPERTY = "com.smartling.tls.enabled";

    private static Logger logger = Logger.getLogger(HttpClient.class.getName());
    private static boolean securityEnabled;

    static {
        boolean envTls = true;
        boolean propTls = true;

        if (System.getenv(TLS_ENVIRONMENT_VAR) != null)
        {
            envTls = Boolean.valueOf(System.getenv(TLS_ENVIRONMENT_VAR));
        }

        if (System.getProperty(TLS_PROPERTY) != null)
        {
            propTls = Boolean.valueOf(System.getProperty(TLS_PROPERTY));
        }

        securityEnabled = !(!envTls || !propTls);
    }

    private final HttpClientSettings settings;

    public HttpClient(final HttpClientSettings settings)
    {
        this.settings = settings;
    }
    ResponseEntity<JsonObject> post(String url, JsonValue data, String requestId) throws IOException
    {
        return post(url, data, requestId, null);
    }

    ResponseEntity<JsonObject> post(String url, JsonValue data, String requestId, String authHeader) throws IOException
    {
        HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection(getConfiguredProxy());
        int status;
        String reason;

        // request
        if (!securityEnabled)
        {
            logger.warning(String.format("TLS disabled, %s not trusted", url));
            conn.setSSLSocketFactory(InsecureTrustManager.insecureSSLSocketFactory());
            conn.setHostnameVerifier(new InsecureHostnameVerifier());
        }
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        if (requestId != null && !requestId.isEmpty())
        {
            conn.setRequestProperty("X-SL-RequestId", requestId);
        }
        if (settings.getProxy().getUser() != null)
        {
            conn.setRequestProperty("Proxy-Authorization", buildProxyAuthorizationHeader());
        }
        if (authHeader != null)
        {
            conn.setRequestProperty("Authorization", authHeader);
        }
        conn.getOutputStream().write(data.toString().getBytes("UTF-8"));

        // response
        status = conn.getResponseCode();
        reason = conn.getResponseMessage();

        logger.finest(String.format("Response code: %d", status));
    
        if (status != 200)
            return new ResponseEntity<JsonObject>(status, reason, null);

        return new ResponseEntity<JsonObject>(status, reason, this.readResponseBody(conn));
    }

    private Proxy getConfiguredProxy()
    {
        HttpClientSettings.Proxy proxySettings = settings.getProxy();
        if (proxySettings == HttpClientSettings.Proxy.NONE)
        {
            return Proxy.NO_PROXY;
        }
        else
        {
            return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxySettings.getHost(), proxySettings.getPort()));
        }
    }

    private String buildProxyAuthorizationHeader() throws UnsupportedEncodingException
    {
        String rawCredentials = settings.getProxy().getUser() + ":" + settings.getProxy().getPassword();
        return "Basic " + new String(Base64Coder.encode(rawCredentials.getBytes("UTF-8")));
    }

    private JsonObject readResponseBody(HttpURLConnection connection) throws IOException
    {
        return Json.parse(new InputStreamReader(connection.getInputStream(), "UTF-8")).asObject();
    }

}
