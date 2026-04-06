package com.smartling.api.filetranslations.v2.pto;

public class Callback
{
    private String url;
    private String httpMethod;
    private String userData;

    public Callback()
    {
    }

    public Callback(String url, String httpMethod, String userData)
    {
        this.url = url;
        this.httpMethod = httpMethod;
        this.userData = userData;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getHttpMethod()
    {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod)
    {
        this.httpMethod = httpMethod;
    }

    public String getUserData()
    {
        return userData;
    }

    public void setUserData(String userData)
    {
        this.userData = userData;
    }

    @Override
    public String toString()
    {
        return "Callback{" +
            "url='" + url + '\'' +
            ", httpMethod='" + httpMethod + '\'' +
            ", userData='" + userData + '\'' +
            '}';
    }
}
