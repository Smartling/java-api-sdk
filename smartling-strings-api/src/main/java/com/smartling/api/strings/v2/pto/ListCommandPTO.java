package com.smartling.api.strings.v2.pto;


public class ListCommandPTO
{
    public static final int MAX_LIMIT = 500;

    private int offset = 0;

    private int limit = MAX_LIMIT;

    public int getOffset()
    {
        return offset;
    }

    public void setOffset(int offset)
    {
        this.offset = offset;
    }

    public int getLimit()
    {
        return limit;
    }

    public void setLimit(int limit)
    {
        this.limit = limit;
    }
}
