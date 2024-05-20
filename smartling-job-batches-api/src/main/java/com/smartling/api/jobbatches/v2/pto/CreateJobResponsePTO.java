package com.smartling.api.jobbatches.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateJobResponsePTO implements ResponseData
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
