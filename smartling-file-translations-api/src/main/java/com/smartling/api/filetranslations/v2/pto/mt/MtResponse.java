package com.smartling.api.filetranslations.v2.pto.mt;


import com.smartling.api.v2.response.ResponseData;

public class MtResponse implements ResponseData
{
    private String mtUid;

    private MtResponse()
    {
    }
    public MtResponse(String mtUid)
    {
        this.mtUid = mtUid;
    }

    public String getMtUid()
    {
        return mtUid;
    }

    public void setMtUid(String mtUid)
    {
        this.mtUid = mtUid;
    }

    @Override
    public String toString()
    {
        return "MtResponse{" +
            "mtUid='" + mtUid + '\'' +
            '}';
    }
}
