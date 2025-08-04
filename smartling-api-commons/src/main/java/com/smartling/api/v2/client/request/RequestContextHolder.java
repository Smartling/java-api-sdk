package com.smartling.api.v2.client.request;

public class RequestContextHolder
{
    private static final ThreadLocal<RequestContext> REQUEST_CONTEXT = new ThreadLocal<>();

    public static void setContext(RequestContext requestContext)
    {
        REQUEST_CONTEXT.set(requestContext);
    }

    public static RequestContext getContext()
    {
        return REQUEST_CONTEXT.get();
    }

    public static void clearContext()
    {
        REQUEST_CONTEXT.remove();
    }
}
