package com.smartling.api.v3.jobs.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomFieldPTO implements ResponseData
{
    private String fieldUid;
    private String type;
    private String fieldName;
    private boolean enabled;
    private boolean required;
    private boolean searchable;
    private boolean displayToTranslators;
    private List<String> options;
    private String defaultValue;
    private String description;
}
