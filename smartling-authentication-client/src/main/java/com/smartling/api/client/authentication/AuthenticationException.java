package com.smartling.api.client.authentication;

/**
 * Created by scott on 9/24/15.
 */
public abstract class AuthenticationException extends RuntimeException
{
    AuthenticationException(String s)
    {
        super(s);
    }

    AuthenticationException(String s, Throwable throwable)
    {
        super(s, throwable);
    }
}
