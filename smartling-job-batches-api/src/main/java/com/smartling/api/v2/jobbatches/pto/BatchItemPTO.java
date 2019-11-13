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
public class BatchItemPTO implements ResponseData
{
    private String status;
    private Calendar updatedDate;
    private String errors;
    private String fileUri;
    private List<BatchItemLocalePTO> targetLocales;
}
