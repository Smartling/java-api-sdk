package com.smartling.api.v3.jobs.pto;

import lombok.Data;

import java.util.List;

@Data
public class CustomFieldCreatePTO
{
    private CustomFieldType type;
    private String fieldName;
    private boolean enabled;
    private boolean required;
    private boolean searchable;
    private boolean displayToTranslators;
    private List<String> options;
    private String defaultValue;
    private String description;
}
