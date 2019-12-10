package com.smartling.api.files.v2.pto;

public enum OrderBy
{
    CREATED("created"),
    FILE_URI("fileUri"),
    LAST_UPLOADED("lastUploaded"),
    CREATED_ASC("created_asc"),
    CREATED_DESC("created_desc"),
    FILE_URI_ASC("fileUri_asc"),
    FILE_URI_DESC("fileUri_desc"),
    LAST_UPLOADED_ASC("lastUploaded_asc"),
    LAST_UPLOADED_DESC("lastUploaded_desc");

    private final String value;

    OrderBy(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return value;
    }
}
