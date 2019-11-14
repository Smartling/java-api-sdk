package com.smartling.api.jobbatches.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchItemLocalePTO implements ResponseData
{
    private String localeId;
    private int stringsAdded;
}

