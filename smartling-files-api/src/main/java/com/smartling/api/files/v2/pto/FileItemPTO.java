package com.smartling.api.files.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileItemPTO implements ResponseData
{
    private String fileUri;
    private Date created;
    private Date lastUploaded;
    private String fileType;
    private boolean hasInstructions;
}
