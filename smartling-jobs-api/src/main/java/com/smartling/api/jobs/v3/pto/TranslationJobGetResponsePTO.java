package com.smartling.api.jobs.v3.pto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TranslationJobGetResponsePTO extends TranslationJobResponsePTO
{
    private List<SourceFilePTO> sourceFiles;

    private Long priority;
}
