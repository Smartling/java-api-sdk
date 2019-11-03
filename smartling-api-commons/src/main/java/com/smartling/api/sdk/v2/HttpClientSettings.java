package com.smartling.api.sdk.v2;

/**
 * Groups any configuration related to the underlying HTTP client configuration:
 *  - proxy
 *  - ... (potentially: timeouts, connections reuse, etx)
 */
public class HttpClientSettings
{
    private Proxy proxy = Proxy.NONE;

    public void setProxy(final Proxy proxy)
    {
        this.proxy = validateNotNull(proxy, "Proxy configuration can not be null");
    }

    public Proxy getProxy()
    {
        return proxy;
    }

    public static class Proxy
    {
        public static final Proxy NONE = new Proxy(null, 0, null, null);

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

        public static Proxy anonymous(final String host, final int port)
        {
            validateNotEmpty(host, "Host must not be empty");
            validatePortRange(port);

            return new Proxy(host, port, null, null);
        }

        public static Proxy withAuthentication(final String host, final int port, final String user, final String password)
        {
            validateNotEmpty(host, "Host must not be empty");
            validatePortRange(port);
            validateNotEmpty(user, "User must not be empty");
            validateNotEmpty(password, "Password must not be empty");

            return new Proxy(host, port, user, password);
        }

        public static Proxy newProxy(final String host, final Integer port, final String user, final String password)
        {
            if (host == null || host.isEmpty() || port == null)
            {
                return Proxy.NONE;
            }
            else if (user == null || user.isEmpty())
            {
                return Proxy.anonymous(host, port);
            }
            else
            {
                return Proxy.withAuthentication(host, port, user, password);
            }
        }

        public String getHost()
        {
            return host;
        }

        public int getPort()
        {
            return port;
        }

        public String getUser()
        {
            return user;
        }

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
