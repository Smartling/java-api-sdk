package com.smartling.api.v3.jobs.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AddLocaleCommandPTO
{
    /**
     * Indicates, if add locale request should be processed in synchronous mode
     */
    private Boolean syncContent;
}
