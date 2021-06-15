package com.smartling.api.contexts.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContextPTO implements ResponseData
{
    private String contextUid;
    private String contextType;
    private String name;
    private String created;
}
