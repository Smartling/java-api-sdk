package com.smartling.api.filetranslations.v2.pto.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParseConfigItem
{
    private String instruction;
    private String value;
}
