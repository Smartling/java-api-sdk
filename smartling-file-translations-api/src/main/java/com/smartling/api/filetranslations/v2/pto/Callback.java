package com.smartling.api.filetranslations.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Callback
{
    private String url;
    private String httpMethod;
    private String userData;
}
