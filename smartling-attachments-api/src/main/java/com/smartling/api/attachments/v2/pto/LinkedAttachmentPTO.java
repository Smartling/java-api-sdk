package com.smartling.api.attachments.v2.pto;

public class LinkedAttachmentPTO extends AttachmentPTO
{
    private boolean canDelete;

    public boolean isCanDelete()
    {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete)
    {
        this.canDelete = canDelete;
    }
}
