package com.smartling.api.jobs.v3.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HashcodesAndLocalesPTO
{
    private List<String> hashcodes;
    private List<String> localeIds;

    public HashcodesAndLocalesPTO(List<String> hashcodes)
    {
        this.hashcodes = hashcodes;
    }
}
