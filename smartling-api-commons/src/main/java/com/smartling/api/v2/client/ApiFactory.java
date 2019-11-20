package com.smartling.api.v2.client;

import com.smartling.api.v2.client.auth.AuthorizationRequestFilter;

public interface ApiFactory<T>
{
    T buildApi(final String userIdentifier, final String userSecret);
    T buildApi(AuthorizationRequestFilter filter);
}
