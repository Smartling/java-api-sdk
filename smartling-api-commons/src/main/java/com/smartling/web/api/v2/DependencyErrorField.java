package com.smartling.web.api.v2;

public class DependencyErrorField
{
    private String key;
    private DependencyErrorFieldType type;

    public enum DependencyErrorFieldType
    {
        ID, NAME
    }

    public DependencyErrorField()
    {
        this.key = null;
        this.type = DependencyErrorFieldType.ID;
    }

    public DependencyErrorField(String key, DependencyErrorFieldType type)
    {
        this.key = key;
        this.type = type;
    }

    public String getKey()
    {
        return key;
    }

    public DependencyErrorFieldType getType()
    {
        return type;
    }
}
