package com.smartling.api.strings.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessStatisticsPTO implements Serializable
{
    private int requested;
    private int processed;
    private int errored;
    private int skipped;
}
