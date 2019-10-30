package com.smartling.api.client.authentication;

/**
 * Thrown to indicate that an authentication attempt was made using an invalid user identifier or secret.
 *
 * @author Scott Rossillo
 */
public class AccessDeniedException extends AuthenticationException
{
    public AccessDeniedException(String s)
    {
        super(s);
    }
}
