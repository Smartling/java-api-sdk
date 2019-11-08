package com.smartling.api.v2.issues.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class WatcherTemplatePTO
{
    private String email;
    private String name;
    private Boolean enabled;
}
