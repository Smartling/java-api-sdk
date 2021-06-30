package com.smartling.api.contexts.v2.pto;

import com.smartling.api.v2.response.ListResponse;
import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchBindingPTO implements ResponseData
{
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Error implements ResponseData
    {
        private String message;
    }

    private ListResponse<BindingPTO> created;
    private ListResponse<Error> errors;
}
