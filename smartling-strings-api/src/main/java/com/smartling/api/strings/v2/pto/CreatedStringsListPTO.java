package com.smartling.api.strings.v2.pto;

import com.smartling.api.v2.response.ResponseData;

import java.util.List;

public class CreatedStringsListPTO implements ResponseData
{
    private int wordCount;
    private int stringCount;
    private String processUid;
    private List<CreatedStringPTO> items;

    public int getWordCount()
    {
        return wordCount;
    }

    public void setWordCount(int wordCount)
    {
        this.wordCount = wordCount;
    }

    public int getStringCount()
    {
        return stringCount;
    }

    public void setStringCount(int stringCount)
    {
        this.stringCount = stringCount;
    }

    public String getProcessUid()
    {
        return processUid;
    }

    public void setProcessUid(String processUid)
    {
        this.processUid = processUid;
    }

    public List<CreatedStringPTO> getItems()
    {
        return items;
    }

    public void setItems(List<CreatedStringPTO> items)
    {
        this.items = items;
    }
}
