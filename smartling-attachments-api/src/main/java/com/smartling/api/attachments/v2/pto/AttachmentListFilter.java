package com.smartling.api.attachments.v2.pto;

import javax.ws.rs.QueryParam;
import java.io.Serializable;

public class AttachmentListFilter implements Serializable
{
    @QueryParam("offset")
    private Integer offset;

    @QueryParam("limit")
    private Integer limit;

    public Integer getOffset()
    {
        return offset;
    }

    public void setOffset(Integer offset)
    {
        this.offset = offset;
    }

    public Integer getLimit()
    {
        return limit;
    }

    public void setLimit(Integer limit)
    {
        this.limit = limit;
    }
}
