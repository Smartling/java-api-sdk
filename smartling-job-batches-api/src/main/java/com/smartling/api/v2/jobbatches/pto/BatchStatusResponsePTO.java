package com.smartling.api.v2.jobbatches.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class BatchStatusResponsePTO implements ResponseData
{
    private String status;
    private Boolean authorized;
    private String generalErrors;
    private String translationJobUid;
    private String projectId;
    private Calendar updatedDate;
    private List<BatchItemPTO> files;
}
