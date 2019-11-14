package com.smartling.api.jobs.v3.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomFieldValueChangePTO
{
    private String customFieldUid;
    private String customFieldName;
    private String from;
    private String to;
}
