package com.smartling.api.files.v2.pto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileStatusResponse implements ResponseData
{
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ssXX", timezone="UTC")
    private Date created;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ssXX", timezone="UTC")
    private Date lastUploaded;
    private String fileType;
    private String fileUri;
    private boolean hasInstructions;
    private List<FileStatusItemPTO> items;
    private int parserVersion;
    private int totalCount;
    private int totalStringCount;
    private int totalWordCount;
    private Map<String, Object> directives;
    private NamespacePTO namespace;
}
