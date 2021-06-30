package com.smartling.api.contexts.v2.pto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequestPTO
{
    private String contentFileUri;
    private List<String> stringHashcodes;
    private Integer overrideContextOlderThanDays;
}
