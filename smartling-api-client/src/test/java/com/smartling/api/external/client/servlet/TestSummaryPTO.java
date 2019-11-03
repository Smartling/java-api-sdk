package com.smartling.api.external.client.servlet;

import com.smartling.sdk.v2.ResponseData;
import java.util.List;

public class TestSummaryPTO implements ResponseData
{
    private String       packageUid;
    private String       name;
    private String       createdByUserUid;
    private String       createdDate;
    private String       modifiedByUserUid;
    private String       modifiedDate;
    private String       sourceLocaleId;
    private List<String> targetLocaleIds;

    public String getPackageUid()
    {
        return packageUid;
    }

    public void setPackageUid(final String packageUid)
    {
        this.packageUid = packageUid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getCreatedByUserUid()
    {
        return createdByUserUid;
    }

    public void setCreatedByUserUid(final String createdByUserUid)
    {
        this.createdByUserUid = createdByUserUid;
    }

    public String getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(final String createdDate)
    {
        this.createdDate = createdDate;
    }

    public String getModifiedByUserUid()
    {
        return modifiedByUserUid;
    }

    public void setModifiedByUserUid(final String modifiedByUserUid)
    {
        this.modifiedByUserUid = modifiedByUserUid;
    }

    public String getModifiedDate()
    {
        return modifiedDate;
    }

    public void setModifiedDate(final String modifiedDate)
    {
        this.modifiedDate = modifiedDate;
    }

    public String getSourceLocaleId()
    {
        return sourceLocaleId;
    }

    public void setSourceLocaleId(final String sourceLocaleId)
    {
        this.sourceLocaleId = sourceLocaleId;
    }

    public List<String> getTargetLocaleIds()
    {
        return targetLocaleIds;
    }

    public void setTargetLocaleIds(final List<String> targetLocaleIds)
    {
        this.targetLocaleIds = targetLocaleIds;
    }
}
