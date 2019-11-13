package com.smartling.api.v3.jobs.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class TranslationJobListItemPTO implements ResponseData
{
    private String translationJobUid;
    private String jobName;
    private String jobNumber;
    private String dueDate;
    private List<String> targetLocaleIds;
    private String createdDate;
    private String jobStatus;
    private String referenceNumber;
    private String description;
}
