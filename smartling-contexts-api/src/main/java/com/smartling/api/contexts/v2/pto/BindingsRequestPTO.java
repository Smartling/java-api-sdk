package com.smartling.api.contexts.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BindingsRequestPTO implements ResponseData
{
    private List<BindingRequestPTO> bindings;
}
