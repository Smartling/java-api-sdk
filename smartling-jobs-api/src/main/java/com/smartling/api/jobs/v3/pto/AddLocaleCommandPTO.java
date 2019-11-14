package com.smartling.api.jobs.v3.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddLocaleCommandPTO
{
    /**
     * Indicates, if add locale request should be processed in synchronous mode
     */
    private Boolean syncContent;
}
