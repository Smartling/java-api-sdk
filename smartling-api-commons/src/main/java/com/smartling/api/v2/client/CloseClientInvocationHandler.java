package com.smartling.api.v2.client;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.client.Client;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *
 */
@Slf4j
public class CloseClientInvocationHandler  implements InvocationHandler
{
    private final InvocationHandler subhandler;
    private final Client client;

    CloseClientInvocationHandler(final InvocationHandler subhandler, Client client)
    {
        this.subhandler = subhandler;
        this.client = client;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable
    {
        if ("close".equals(method.getName()) && method.getParameterCount() == 0)
        {
            try
            {
                client.close();
            }
            catch (Exception ex)
            {
                log.error("Error on closing a client.", ex);
            }

            return null;
        }

        return subhandler.invoke(proxy, method, args);
    }
}
