package com.smartling.api.v2.locales.pto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * PTO class to provide basic info about locale, which is Id and Description.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ExtendedLocalePTO extends LocalePTO
{
    private List<String> pluralTags;
}
