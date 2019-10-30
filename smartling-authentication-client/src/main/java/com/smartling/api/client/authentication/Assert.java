package com.smartling.api.client.authentication;

/**
 * Created by scott on 9/24/15.
 */
class Assert
{
    static void notNull(Object o, String message)
    {
        if (o == null)
            throw new IllegalStateException(message);
    }

    static void notNegative(int i, String message)
    {
        if (i < 0)
            throw new IllegalStateException(message);
    }

    private Assert()
    {

    }
}
