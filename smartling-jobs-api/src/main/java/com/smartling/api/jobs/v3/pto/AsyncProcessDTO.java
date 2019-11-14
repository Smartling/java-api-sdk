package com.smartling.api.jobs.v3.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsyncProcessDTO implements ResponseData
{
    private String processUid;
    private String translationJobUid;
    private AsyncProcessState processState;
    private Date createdDate;
    private Date modifiedDate;
}
