package com.smartling.api.strings.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CreatedStringsListPTO implements ResponseData
{
    private int wordCount;
    private int stringCount;
    private String processUid;
    private List<CreatedStringPTO> items;
}
