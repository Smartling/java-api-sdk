package com.smartling.api.jobbatches.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateJobRequestPTO
{
    private String nameTemplate;
    private String description;
    private List<String> targetLocaleIds;

    @Builder.Default
    private JobCreationMode mode = JobCreationMode.REUSE_EXISTING;

    @Builder.Default
    private JobCreationSalt salt = JobCreationSalt.RANDOM_ALPHANUMERIC;

    private String timeZoneName;
}
