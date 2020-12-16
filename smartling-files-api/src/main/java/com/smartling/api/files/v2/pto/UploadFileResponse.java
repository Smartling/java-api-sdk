package com.smartling.api.files.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileResponse implements ResponseData
{
    private Boolean overWritten;
    private Integer stringCount;
    private Integer wordCount;
    private String message;
    private String fileUri;
}
