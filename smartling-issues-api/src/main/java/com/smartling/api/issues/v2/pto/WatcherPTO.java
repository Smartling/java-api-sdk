package com.smartling.api.issues.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class WatcherPTO implements ResponseData
{
    private String uid;
    private String email;
    private String name;
    private Boolean enabled;
    private Date created;
    private Date modified;
}
