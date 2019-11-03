package com.smartling.api.v2.client.servlet;

import com.smartling.api.sdk.v2.response.ResponseData;
import java.util.List;

public class TestListPTO implements ResponseData
{
    private List<TestSummaryPTO> items;

    public List<TestSummaryPTO> getItems()
    {
        return items;
    }

    public void setItems(final List<TestSummaryPTO> items)
    {
        this.items = items;
    }
}
