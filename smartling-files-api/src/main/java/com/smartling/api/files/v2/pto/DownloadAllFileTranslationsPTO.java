package com.smartling.api.files.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.ws.rs.QueryParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DownloadAllFileTranslationsPTO
{
    @QueryParam("fileUri")
    String fileUri;

    @QueryParam("retrievalType")
    private RetrievalType retrievalType;

    @QueryParam("includeOriginalStrings")
    private Boolean includeOriginalStrings;

    @QueryParam("zipFileName")
    private String zipFileName;
}

