package com.smartling.api.client.authentication;

/**
 * Thrown to indicate a problem occurred connecting to the authentication server.
 *
 * @author Scott Rossillo
 */
public class AuthenticationConnectionException extends AuthenticationException
{
    public AuthenticationConnectionException(String s)
    {
        super(s);
    }

    public AuthenticationConnectionException(String s, Throwable throwable)
    {
        super(s, throwable);
    }
}
