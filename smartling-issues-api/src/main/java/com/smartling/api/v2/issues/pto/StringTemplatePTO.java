package com.smartling.api.v2.issues.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@Data
@EqualsAndHashCode
public class StringTemplatePTO
{
    private String hashcode;
    private String localeId;
}
