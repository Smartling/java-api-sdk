package com.smartling.api.client.authentication;

/**
 * Thrown to indicate the client provided invalid input for an authentication request.
 *
 * @author Scott Rossillo
 */
public class BadRequestException extends AuthenticationException
{
    /**
     * Creates a new bad request exception with given message.
     */
    public BadRequestException(String s)
    {
        super(s);
    }
}
