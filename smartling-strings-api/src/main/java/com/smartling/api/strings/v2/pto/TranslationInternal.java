package com.smartling.api.strings.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslationInternal
{
    private String pluralForm;
    private String translation;
    private String modifiedDate;
}
