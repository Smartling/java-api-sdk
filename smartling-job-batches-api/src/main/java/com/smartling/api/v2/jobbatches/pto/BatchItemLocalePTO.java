package com.smartling.api.v2.jobbatches.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class BatchItemLocalePTO implements ResponseData
{
    private String localeId;
    private int stringsAdded;
    private int stringsSkipped;
}
