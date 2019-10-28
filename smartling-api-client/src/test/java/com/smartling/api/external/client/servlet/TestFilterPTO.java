package com.smartling.api.external.client.servlet;

import java.util.List;
import javax.ws.rs.QueryParam;

public class TestFilterPTO
{
    @QueryParam("sourceLocaleId")
    private String sourceLocaleId;

    @QueryParam("tmUid")
    private String tmUid;

    @QueryParam("leverageUid")
    private String leverageUid;

    @QueryParam("targetLocaleId")
    private List<String> targetLocaleId; // AND relationship

    @QueryParam("styleGuideUid")
    private String styleGuideUid;

    @QueryParam("glossaryUid")
    private String glossaryUid;

    @QueryParam("name")
    private String name;

    public static TestFilterPTO of(final String sourceLocaleId, final String tmUid, final String leverageUid, final List<String> targetLocaleId,
            final String styleGuideUid, final String glossaryUid, final String name)
    {
        final TestFilterPTO filter = new TestFilterPTO();
        filter.setGlossaryUid(glossaryUid);
        filter.setLeverageUid(leverageUid);
        filter.setSourceLocaleId(sourceLocaleId);
        filter.setStyleGuideUid(styleGuideUid);
        filter.setTargetLocaleId(targetLocaleId);
        filter.setTmUid(tmUid);
        filter.setName(name);
        return filter;
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

    public String getLeverageUid()
    {
        return leverageUid;
    }

    public void setLeverageUid(final String leverageUid)
    {
        this.leverageUid = leverageUid;
    }

    public List<String> getTargetLocaleId()
    {
        return targetLocaleId;
    }

    public void setTargetLocaleId(final List<String> targetLocaleId)
    {
        this.targetLocaleId = targetLocaleId;
    }

    public String getStyleGuideUid()
    {
        return styleGuideUid;
    }

    public void setStyleGuideUid(final String styleGuideUid)
    {
        this.styleGuideUid = styleGuideUid;
    }

    public String getGlossaryUid()
    {
        return glossaryUid;
    }

    public void setGlossaryUid(final String glossaryUid)
    {
        this.glossaryUid = glossaryUid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }
}
