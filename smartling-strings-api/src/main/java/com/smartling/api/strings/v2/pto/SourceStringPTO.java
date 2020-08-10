package com.smartling.api.strings.v2.pto;


import com.smartling.api.v2.response.ResponseData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SourceStringPTO implements ResponseData
{
    private String hashcode;
    private String stringText;
    private String parsedStringText;
    private String stringVariant;
    private Integer maxLength;
    private List<StringKeyPTO> keys;
}
