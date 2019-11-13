package com.smartling.api.v3.jobs.pto;

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
