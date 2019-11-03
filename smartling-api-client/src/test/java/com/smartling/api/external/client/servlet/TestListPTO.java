package com.smartling.api.external.client.servlet;

import com.smartling.sdk.v2.ResponseData;
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
