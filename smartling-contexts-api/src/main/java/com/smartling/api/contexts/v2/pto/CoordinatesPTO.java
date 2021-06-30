package com.smartling.api.contexts.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordinatesPTO implements ResponseData
{
    private int top;
    private int left;
    private int width;
    private int height;
}
