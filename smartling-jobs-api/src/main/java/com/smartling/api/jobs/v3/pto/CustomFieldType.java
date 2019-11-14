package com.smartling.api.jobs.v3.pto;

public enum CustomFieldType
{
    SHORT_TEXT((short) 1),
    LONG_TEXT((short) 2),
    SELECTBOX((short) 3),
    CHECKBOX((short) 4);

    private short value;

    private CustomFieldType(short value)
    {
        this.value = value;
    }

    public static CustomFieldType valueOf(short value)
    {
        for (CustomFieldType fieldType : values())
        {
            if (fieldType.value == value)
                return fieldType;
        }

        return null;
    }

    public short getValue()
    {
        return value;
    }
}
