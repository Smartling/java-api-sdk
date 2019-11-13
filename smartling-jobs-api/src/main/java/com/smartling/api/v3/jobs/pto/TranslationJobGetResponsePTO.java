package com.smartling.api.v3.jobs.pto;

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
}
