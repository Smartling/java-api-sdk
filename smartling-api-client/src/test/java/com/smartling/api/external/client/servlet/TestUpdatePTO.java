package com.smartling.api.external.client.servlet;

import com.smartling.sdk.v2.ResponseData;
import java.util.LinkedList;
import java.util.List;

public class TestUpdatePTO implements ResponseData
{
    private String  name;
    private String  description;
    private String  tmUid;
    private String  leverageUid;
    private boolean associateTmWithLeverage;

    private List<String>          targetLocaleIds = new LinkedList<>();
    private List<TestGuidePTO>    styleGuides     = new LinkedList<>();
    private List<TestGlossaryPTO> glossaries      = new LinkedList<>();

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

    public String getTmUid()
    {
        return tmUid;
    }

    public void setTmUid(final String tmUid)
    {
        this.tmUid = tmUid;
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

    public List<TestGlossaryPTO> getGlossaries()
    {
        return glossaries;
    }

    public void setGlossaries(final List<TestGlossaryPTO> glossaries)
    {
        this.glossaries = glossaries;
    }

    public boolean isAssociateTmWithLeverage()
    {
        return associateTmWithLeverage;
    }

    public void setAssociateTmWithLeverage(final boolean associateTmWithLeverage)
    {
        this.associateTmWithLeverage = associateTmWithLeverage;
    }
}
