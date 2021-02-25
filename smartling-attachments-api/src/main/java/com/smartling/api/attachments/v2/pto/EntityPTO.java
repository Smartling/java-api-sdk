package com.smartling.api.attachments.v2.pto;

import com.smartling.api.v2.response.ResponseData;

public class EntityPTO implements ResponseData
{
    private String entityUid;
    private String createdByUserUid;

    public String getEntityUid()
    {
        return entityUid;
    }

    public void setEntityUid(String entityUid)
    {
        this.entityUid = entityUid;
    }

    public String getCreatedByUserUid()
    {
        return createdByUserUid;
    }

    public void setCreatedByUserUid(String createdByUserUid)
    {
        this.createdByUserUid = createdByUserUid;
    }
}
