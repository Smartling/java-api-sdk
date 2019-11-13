package com.smartling.api.v3.jobs.pto;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Accessors(chain = true)
public class TranslationJobGetResponsePTO extends TranslationJobResponsePTO
{
    private List<SourceFilePTO> sourceFiles;
}
