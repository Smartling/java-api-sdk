package com.smartling.api.v3.jobs.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomFieldUpdatePTO
{
    private String fieldName;
    private boolean enabled;
    private boolean required;
    private boolean searchable;
    private boolean displayToTranslators;
    private List<String> options;
    private String defaultValue;
    private String description;
}
