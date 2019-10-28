package com.smartling.api.external.client.servlet;

import com.smartling.web.api.v2.ResponseData;
import java.util.List;

public class TestDetailsPTO implements ResponseData
{
    private String                packageUid;
    private String                name;
    private String                description;
    private String                createdByUserUid;
    private String                createdDate;
    private String                modifiedByUserUid;
    private String                modifiedDate;
    private String                sourceLocaleId;
    private String                tmUid;
    private String                leverageUid;
    private List<TestGlossaryPTO> glossaries;
    private List<String>          targetLocaleIds;
    private List<TestGuidePTO>    styleGuides;

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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
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

    public String getTmUid()
    {
        return tmUid;
    }

    public void setTmUid(final String tmUid)
    {
        this.tmUid = tmUid;
    }

    public List<TestGlossaryPTO> getGlossaries()
    {
        return glossaries;
    }

    public void setGlossaries(final List<TestGlossaryPTO> glossaries)
    {
        this.glossaries = glossaries;
    }

    public String getLeverageUid()
    {
        return leverageUid;
    }

    public void setLeverageUid(final String leverageUid)
    {
        this.leverageUid = leverageUid;
    }

    public List<String> getTargetLocaleIds()
    {
        return targetLocaleIds;
    }

    public void setTargetLocaleIds(final List<String> targetLocaleIds)
    {
        this.targetLocaleIds = targetLocaleIds;
    }

    public List<TestGuidePTO> getStyleGuides()
    {
        return styleGuides;
    }

    public void setStyleGuides(final List<TestGuidePTO> styleGuides)
    {
        this.styleGuides = styleGuides;
    }
}
