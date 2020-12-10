package com.smartling.api.jobs.v3.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class TranslationJobFoundByStringsAndLocalesResponsePTO implements ResponseData
{
    private String translationJobUid;
    private String jobName;
    private String dueDate;
    private List<HashcodesByLocalePTO> hashcodesByLocale;
}
