package com.smartling.api.strings.v2.pto;

import java.util.List;

public class StringsCreationPTO
{
    public static final String DEFAULT_NAMESPACE = "smartling.strings-api.default.namespace";

    private String placeholderFormat;
    private String placeholderFormatCustom;
    private String namespace = DEFAULT_NAMESPACE;
    private List<StringCreationPTO> strings;

    public String getPlaceholderFormat()
    {
        return placeholderFormat;
    }

    public void setPlaceholderFormat(String placeholderFormat)
    {
        this.placeholderFormat = placeholderFormat;
    }

    public String getPlaceholderFormatCustom()
    {
        return placeholderFormatCustom;
    }

    public void setPlaceholderFormatCustom(String placeholderFormatCustom)
    {
        this.placeholderFormatCustom = placeholderFormatCustom;
    }

    public String getNamespace()
    {
        return namespace;
    }

    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }

    public List<StringCreationPTO> getStrings()
    {
        return strings;
    }

    public void setStrings(List<StringCreationPTO> strings)
    {
        this.strings = strings;
    }
}
