package com.smartling.sdk.authorization.pto;

import com.smartling.api.sdk.v2.response.ResponseData;


public class UserIdentityPTO implements ResponseData
{
    private String accountUid;
    private String projectUid;
    private String agencyUid;
    private String userUid;
    private String roleName;

    public String getAccountUid()
    {
        return accountUid;
    }

    public UserIdentityPTO setAccountUid(String accountUid)
    {
        this.accountUid = accountUid;
        return this;
    }

    public String getProjectUid()
    {
        return projectUid;
    }

    public UserIdentityPTO setProjectUid(String projectUid)
    {
        this.projectUid = projectUid;
        return this;
    }

    public String getAgencyUid()
    {
        return agencyUid;
    }

    public UserIdentityPTO setAgencyUid(String agencyUid)
    {
        this.agencyUid = agencyUid;
        return this;
    }

    public String getUserUid()
    {
        return userUid;
    }

    public UserIdentityPTO setUserUid(String userUid)
    {
        this.userUid = userUid;
        return this;
    }

    public String getRoleName()
    {
        return roleName;
    }

    public UserIdentityPTO setRoleName(String roleName)
    {
        this.roleName = roleName;
        return this;
    }
}
