package com.smartling.api.strings.v2.pto;


import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceStringListPTO implements ResponseData
{
    private List<SourceStringPTO> items;
}
