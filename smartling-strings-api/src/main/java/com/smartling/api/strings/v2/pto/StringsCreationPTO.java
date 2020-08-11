package com.smartling.api.strings.v2.pto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class StringsCreationPTO
{
    public static final String DEFAULT_NAMESPACE = "smartling.strings-api.default.namespace";

    private String placeholderFormat;
    private String placeholderFormatCustom;
    private String namespace = DEFAULT_NAMESPACE;
    private List<StringCreationPTO> strings;
}
