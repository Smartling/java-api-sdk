package com.smartling.api.v2.issues.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class StringFilterPTO
{
    private Collection<String> hashcodes;
    private Collection<String> localeIds;
}
