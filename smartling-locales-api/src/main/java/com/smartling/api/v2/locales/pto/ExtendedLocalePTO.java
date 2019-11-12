package com.smartling.api.v2.locales.pto;

import java.util.List;

/**
 * PTO class to provide basic info about locale, which is Id and Description.
 */
public class ExtendedLocalePTO extends LocalePTO
{
    private List<String> pluralTags;

    public List<String> getPluralTags()
    {
        return pluralTags;
    }

    public void setPluralTags(List<String> pluralTags)
    {
        this.pluralTags = pluralTags;
    }
}
