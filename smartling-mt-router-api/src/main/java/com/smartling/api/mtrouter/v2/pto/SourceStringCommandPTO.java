package com.smartling.api.mtrouter.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SourceStringCommandPTO
{
    private String key;
    private String sourceText;
}
