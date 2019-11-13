package com.smartling.api.v3.jobs.pto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomFieldValueChangePTO
{
    private String customFieldUid;
    private String customFieldName;
    private String from;
    private String to;
}
