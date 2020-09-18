package com.smartling.api.strings.v2.pto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class StringCreationPTO implements Serializable
{
    private String format;
    private String variant;
    private String stringText;
    private String instruction;
    private String callbackUrl;
    private String callbackMethod;
}
