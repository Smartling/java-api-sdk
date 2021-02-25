package com.smartling.api.attachments.v2.pto;

import com.smartling.api.v2.response.ResponseData;

public class EntityCountAttachmentsPTO implements ResponseData
{
    private String entityUid;
    private Integer attachmentCount;

    public EntityCountAttachmentsPTO()
    {
    }

    public EntityCountAttachmentsPTO(String entityUid, Integer attachmentCount)
    {
        this.entityUid = entityUid;
        this.attachmentCount = attachmentCount;
    }

    public String getEntityUid()
    {
        return entityUid;
    }

    public void setEntityUid(String entityUid)
    {
        this.entityUid = entityUid;
    }

    public Integer getAttachmentCount()
    {
        return attachmentCount;
    }

    public void setAttachmentCount(Integer attachmentCount)
    {
        this.attachmentCount = attachmentCount;
    }
}
