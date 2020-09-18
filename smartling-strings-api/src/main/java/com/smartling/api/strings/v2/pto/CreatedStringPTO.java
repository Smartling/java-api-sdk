package com.smartling.api.strings.v2.pto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreatedStringPTO
{
    private String variant;
    private String hashcode;
    private String stringText;
    private String parsedStringText;
    private boolean overWritten = false;
}
