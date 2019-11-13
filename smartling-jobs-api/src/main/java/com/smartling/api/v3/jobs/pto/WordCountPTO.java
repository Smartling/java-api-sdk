package com.smartling.api.v3.jobs.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class WordCountPTO
{
    private long stringCount = 0;
    private long wordCount   = 0;
}
