package com.smartling.api.files.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileLocaleStatusResponse implements ResponseData
{
    private String fileUri;
    private Date lastUploaded;
    private String fileType;
    private int totalStringCount;
    private int totalWordCount;
    private int authorizedStringCount;
    private int authorizedWordCount;
    private int completedStringCount;
    private int completedWordCount;
    private int excludedStringCount;
    private int excludedWordCount;
    private Date created;
    private boolean hasInstructions;
    private int parserVersion;
    private Map<String, Object> directives;
    private NamespacePTO namespace;
}
