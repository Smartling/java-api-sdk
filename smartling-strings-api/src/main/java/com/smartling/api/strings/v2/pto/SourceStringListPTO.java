package com.smartling.api.strings.v2.pto;


import com.smartling.api.v2.response.ResponseData;

import java.util.List;

public class SourceStringListPTO implements ResponseData
{
    private List<SourceStringPTO> items;

    public List<SourceStringPTO> getItems()
    {
        return items;
    }

    public void setItems(List<SourceStringPTO> items)
    {
        this.items = items;
    }
}
