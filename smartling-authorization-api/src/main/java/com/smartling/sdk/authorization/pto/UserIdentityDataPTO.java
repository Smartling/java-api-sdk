package com.smartling.sdk.authorization.pto;

import com.smartling.sdk.v2.ResponseData;

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
