package com.smartling.resteasy.ext.data;

import com.smartling.resteasy.ext.ListFormParam;
import lombok.Data;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import java.util.List;

@Data
public class FormWithListParams
{
    public static final String LIST_PARAM_NAME = "listParamName[]";

    @ListFormParam(LIST_PARAM_NAME)
    @PartType("text/plain")
    private List<Object> listField;
}
