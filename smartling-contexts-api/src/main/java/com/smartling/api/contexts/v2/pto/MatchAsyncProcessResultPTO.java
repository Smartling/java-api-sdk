package com.smartling.api.contexts.v2.pto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(as = MatchAsyncProcessResultPTO.class)
public class MatchAsyncProcessResultPTO implements AsyncProcessResultPTO
{
    private String contextUid;
    private List<BindingPTO> bindings;
    private Long checkedStringsCount;
}
