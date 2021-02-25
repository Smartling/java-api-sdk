package com.smartling.api.attachments.v2.pto;

import java.util.List;

public class EntityLinkCommand extends EntityListCommand
{
    private List<String> attachmentUids;

    public List<String> getAttachmentUids()
    {
        return attachmentUids;
    }

    public void setAttachmentUids(List<String> attachmentUids)
    {
        this.attachmentUids = attachmentUids;
    }
}
