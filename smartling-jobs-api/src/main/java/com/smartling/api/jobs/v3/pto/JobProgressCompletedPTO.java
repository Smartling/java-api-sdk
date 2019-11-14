package com.smartling.api.jobs.v3.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobProgressCompletedPTO implements ResponseData
{
    private int totalWordCount;
    private int percentComplete;
}
