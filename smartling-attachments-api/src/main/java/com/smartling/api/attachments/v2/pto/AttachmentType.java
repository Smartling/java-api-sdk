package com.smartling.api.attachments.v2.pto;

public enum AttachmentType
{
    JOBS(1),
    ISSUES(2),
    STRINGS(3);

    private int value;

    AttachmentType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static AttachmentType valueOf(Integer value)
    {
        if (null != value)
        {
            for (AttachmentType attachmentType : values())
            {
                if (attachmentType.value == value)
                    return attachmentType;
            }
        }
        return null;
    }
}
