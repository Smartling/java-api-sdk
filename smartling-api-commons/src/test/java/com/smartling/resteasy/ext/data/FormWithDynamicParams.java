package com.smartling.resteasy.ext.data;

import com.smartling.resteasy.ext.DynamicFormParam;
import lombok.Data;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import java.util.Map;

@Data
public class FormWithDynamicParams
{
    @DynamicFormParam()
    @PartType("text/plain")
    private Map<String, Object> dynamicFields;
}
