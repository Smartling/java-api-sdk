package com.smartling.api.attachments.v2.pto;

import java.util.List;

public class AttachmentEntitiesPTO extends AttachmentPTO
{
    private List<EntityPTO> entities;

    public List<EntityPTO> getEntities()
    {
        return entities;
    }

    public void setEntities(List<EntityPTO> entities)
    {
        this.entities = entities;
    }
}
