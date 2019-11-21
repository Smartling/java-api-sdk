package com.smartling.api.client.context;

/**
 * Configuration settings for the underlying HTTP client.
 *
 *  Enables proxy configuration, and potentially, timeouts, connection reuse, etc.
 */
public class HttpClientSettings
{
    private Proxy proxy = Proxy.NONE;

    /**
     * Sets the proxy server configuration.
     *
     * @param proxy the <code>Proxy</code> configuration
     */
    public void setProxy(final Proxy proxy)
    {
        this.proxy = validateNotNull(proxy, "Proxy configuration can not be null");
    }

    /**
     * Returns the current proxy configuration.
     *
     * @return the current <code>Proxy</code> configuration
     */
    public Proxy getProxy()
    {
        return proxy;
    }

    /**
     * Configuration settings for configuring the underlying HTTP client to use a proxy server.
     */
    public static class Proxy
    {
        static final Proxy NONE = new Proxy(null, 0, null, null);

        private final String host;
        private final int port;
        private final String user;
        private final String password;

        private Proxy(final String host, final int port, final String user, final String password)
        {
            this.host = host;
            this.port = port;
            this.user = user;
            this.password = password;
        }

        /**
         * Creates a new anonymous proxy.
         *
         * @param host the proxy host (required)
         * @param port the proxy port (required)
         *
         * @return a new proxy configuration
         */
        public static Proxy anonymous(final String host, final int port)
        {
            validateNotEmpty(host, "Host must not be empty");
            validatePortRange(port);

            return new Proxy(host, port, null, null);
        }

        /**
         * Creates a new proxy authenticated with HTTP basic authentication.
         *
         * @param host the proxy host (required)
         * @param port the proxy port (required)
         * @param user the proxy user name (required)
         * @param password the proxy password (required)
         *
         * @return a new proxy configuration
         */
        public static Proxy withAuthentication(final String host, final int port, final String user, final String password)
        {
            validateNotEmpty(host, "Host must not be empty");
            validatePortRange(port);
            validateNotEmpty(user, "User must not be empty");
            validateNotEmpty(password, "Password must not be empty");

            return new Proxy(host, port, user, password);
        }

        /**
         * Returns the proxy server host.
         *
         * @return the proxy host
         */
        public String getHost()
        {
            return host;
        }

        /**
         * Returns the proxy server port.
         *
         * @return the proxy port
         */
        public int getPort()
        {
            return port;
        }

        /**
         * Returns the proxy server user name.
         *
         * @return the proxy user name
         */
        public String getUser()
        {
            return user;
        }

        /**
         * Returns the proxy server password.
         *
         * @return the proxy password
         */
        public String getPassword()
        {
            return password;
        }
    }

    private static void validatePortRange(final int port)
    {
        if (port <= 0 || port >= 65535)
        {
            throw new IllegalArgumentException("Port is out of range");
        }
    }

    private static void validateNotEmpty(final String value, final String message)
    {
        if (value == null || value.isEmpty())
        {
            throw new IllegalArgumentException(message);
        }
    }

    private static <T> T validateNotNull(final T object, final String message)
    {
        if (object == null)
        {
            throw new IllegalArgumentException(message);
        }

        return object;
    }
}
