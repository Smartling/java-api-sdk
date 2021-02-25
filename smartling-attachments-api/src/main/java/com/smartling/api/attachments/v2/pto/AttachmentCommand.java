package com.smartling.api.attachments.v2.pto;

import java.io.Serializable;
import java.util.List;

public class AttachmentCommand implements Serializable
{
    private String name;
    private String description;
    private List<String> entityUids;

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

    public List<String> getEntityUids()
    {
        return entityUids;
    }

    public void setEntityUids(List<String> entityUids)
    {
        this.entityUids = entityUids;
    }
}
