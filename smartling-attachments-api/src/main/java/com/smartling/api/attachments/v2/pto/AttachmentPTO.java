package com.smartling.api.attachments.v2.pto;

import com.smartling.api.v2.response.ResponseData;

import java.util.Date;

public class AttachmentPTO implements ResponseData
{
    private String attachmentUid;
    private String name;
    private String description;
    private Date createdDate;
    private String createdByUserUid;

    public String getAttachmentUid()
    {
        return attachmentUid;
    }

    public void setAttachmentUid(String attachmentUid)
    {
        this.attachmentUid = attachmentUid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
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
