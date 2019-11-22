package com.smartling.api.jobbatches.v1.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchItemPTO implements ResponseData
{
    private String status;
    private Calendar updatedDate;
    private String errors;
    private String fileUri;
    private List<BatchItemLocalePTO> targetLocales;
}
