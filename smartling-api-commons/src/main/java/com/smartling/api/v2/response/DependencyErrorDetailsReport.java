package com.smartling.api.v2.response;

import java.util.ArrayList;
import java.util.List;

public class DependencyErrorDetailsReport
{
    private List<DependencyErrorField> fields;
    private List<List<Object>> items;

    public DependencyErrorDetailsReport(List<DependencyErrorField> fields)
    {
        this.fields = fields;
        items = new ArrayList<List<Object>>();
    }

    public DependencyErrorDetailsReport()
    {
        this.fields = new ArrayList<DependencyErrorField>();
        items = new ArrayList<List<Object>>();
    }

    public List<DependencyErrorField> getFields()
    {
        return fields;
    }

    public void setFields(List<DependencyErrorField> fields)
    {
        this.fields = fields;
    }

    public List<List<Object>> getItems()
    {
        return items;
    }

    public void setItems(List<List<Object>> items)
    {
        this.items = items;
    }
}
