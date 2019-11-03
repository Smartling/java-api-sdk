package com.smartling.web.api.v2;

import com.smartling.sdk.v2.ResponseData;

import java.util.Collections;
import java.util.List;

public class ListResponse<T extends ResponseData> implements ResponseData
{
    private long totalCount = 0;
    private List<T> items = Collections.emptyList();

    public ListResponse()
    {
    }

    public ListResponse(long totalCount, List<T> items)
    {
        this.totalCount = totalCount;
        this.items = items;
    }

    public ListResponse(List<T> items)
    {
        this(items.size(), items);
    }

    public List<T> getItems()
    {
        return items;
    }

    public void setItems(List<T> items)
    {
        this.items = items;
    }

    public long getTotalCount()
    {
        return totalCount;
    }

    public void setTotalCount(long totalCount)
    {
        this.totalCount = totalCount;
    }
}
