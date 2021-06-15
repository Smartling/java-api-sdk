package com.smartling.api.contexts.v2.pto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartling.api.v2.response.ResponseData;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginatedListResponse<T extends ResponseData> implements ResponseData
{
    private List<T> items;
    private String  offset;
}
