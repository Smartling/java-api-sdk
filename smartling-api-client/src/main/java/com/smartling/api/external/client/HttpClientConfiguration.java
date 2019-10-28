package com.smartling.api.external.client;

import java.util.Objects;

/**
 * Configuration for the underlying http client of the proxy.
 *
 * All timeouts are in milliseconds.  The defaults here are designed
 * for a service with a relatively low number of long running requests.
 */
public class HttpClientConfiguration
{
    public static final int DEFAULT_SOCKET_TIMEOUT = 10_000;
    public static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 60_000;
    public static final int DEFAULT_CONNECTION_TIMEOUT = 10_000;
    public static final int MAX_THREAD_PER_ROUTE = 20;
    public static final int MAX_THREAD_TOTAL = 20;

    private int     connectionRequestTimeout    = DEFAULT_CONNECTION_REQUEST_TIMEOUT;
    private int     connectionTimeout           = DEFAULT_CONNECTION_TIMEOUT;
    private int     maxThreadPerRoute           = MAX_THREAD_PER_ROUTE;
    private int     maxThreadTotal              = MAX_THREAD_TOTAL;
    private int     socketTimeout               = DEFAULT_SOCKET_TIMEOUT;
    private boolean staleConnectionCheckEnabled = true;
    private String  proxyHost;
    private Integer proxyPort;
    private String  proxyUser;
    private String  proxyPassword;

    public int getConnectionRequestTimeout()
    {
        return connectionRequestTimeout;
    }

    public HttpClientConfiguration setConnectionRequestTimeout(final int connectionRequestTimeout)
    {
        this.connectionRequestTimeout = connectionRequestTimeout;
        return this;
    }

    public int getConnectionTimeout()
    {
        return connectionTimeout;
    }

    public HttpClientConfiguration setConnectionTimeout(final int connectionTimeout)
    {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public int getMaxThreadPerRoute()
    {
        return maxThreadPerRoute;
    }

    public HttpClientConfiguration setMaxThreadPerRoute(final int maxThreadPerRoute)
    {
        this.maxThreadPerRoute = maxThreadPerRoute;
        return this;
    }

    public int getMaxThreadTotal()
    {
        return maxThreadTotal;
    }

    public HttpClientConfiguration setMaxThreadTotal(final int maxThreadTotal)
    {
        this.maxThreadTotal = maxThreadTotal;
        return this;
    }

    public int getSocketTimeout()
    {
        return socketTimeout;
    }

    public HttpClientConfiguration setSocketTimeout(final int socketTimeout)
    {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public boolean isStaleConnectionCheckEnabled()
    {
        return staleConnectionCheckEnabled;
    }

    public HttpClientConfiguration setStaleConnectionCheckEnabled(final boolean staleConnectionCheckEnabled)
    {
        this.staleConnectionCheckEnabled = staleConnectionCheckEnabled;
        return this;
    }

    public String getProxyHost()
    {
        return proxyHost;
    }

    public HttpClientConfiguration setProxyHost(final String proxyHost)
    {
        this.proxyHost = Objects.requireNonNull(proxyHost, "Proxy host must not be empty");
        return this;
    }

    public Integer getProxyPort()
    {
        return proxyPort;
    }

    public HttpClientConfiguration setProxyPort(final int proxyPort)
    {
        this.proxyPort = proxyPort;
        return this;
    }

    public String getProxyUser()
    {
        return proxyUser;
    }

    public HttpClientConfiguration setProxyUser(final String proxyUser)
    {
        this.proxyUser = Objects.requireNonNull(proxyUser, "Proxy user must not be empty");
        return this;
    }

    public String getProxyPassword()
    {
        return proxyPassword;
    }

    public HttpClientConfiguration setProxyPassword(final String proxyPassword)
    {
        this.proxyPassword = Objects.requireNonNull(proxyPassword, "Proxy password must not be empty");
        return this;
    }
}
