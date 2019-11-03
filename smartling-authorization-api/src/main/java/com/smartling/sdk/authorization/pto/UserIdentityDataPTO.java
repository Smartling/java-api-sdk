package com.smartling.sdk.authorization.pto;

import com.smartling.api.v2.response.ResponseData;

public class UserIdentityDataPTO implements ResponseData
{
    private UserIdentityPTO userIdentity;

    public UserIdentityPTO getUserIdentity()
    {
        return userIdentity;
    }

    public void setUserIdentity(UserIdentityPTO userIdentity)
    {
        this.userIdentity = userIdentity;
    }
}
