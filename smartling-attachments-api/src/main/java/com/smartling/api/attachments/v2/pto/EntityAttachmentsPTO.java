package com.smartling.api.attachments.v2.pto;

import com.smartling.api.v2.response.ResponseData;

import java.util.List;

public class EntityAttachmentsPTO implements ResponseData
{
    private String entityUid;
    private List<LinkedAttachmentPTO> attachments;

    public EntityAttachmentsPTO()
    {
    }

    public EntityAttachmentsPTO(String entityUid, List<LinkedAttachmentPTO> attachments)
    {
        this.entityUid = entityUid;
        this.attachments = attachments;
    }

    public String getEntityUid()
    {
        return entityUid;
    }

    public void setEntityUid(String entityUid)
    {
        this.entityUid = entityUid;
    }

    public List<LinkedAttachmentPTO> getAttachments()
    {
        return attachments;
    }

    public void setAttachments(List<LinkedAttachmentPTO> attachments)
    {
        this.attachments = attachments;
    }
}
