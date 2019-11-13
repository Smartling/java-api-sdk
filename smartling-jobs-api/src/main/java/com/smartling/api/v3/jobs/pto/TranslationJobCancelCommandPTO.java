package com.smartling.api.v3.jobs.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@AllArgsConstructor
public class TranslationJobCancelCommandPTO
{
    private String reason;
}
