package com.smartling.api.attachments.v2.pto;

import java.io.Serializable;
import java.util.List;

public class EntityListCommand implements Serializable
{
    private List<String> entityUids;

    public List<String> getEntityUids()
    {
        return entityUids;
    }

    public void setEntityUids(List<String> entityUids)
    {
        this.entityUids = entityUids;
    }
}
