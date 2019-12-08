package com.smartling.api.files.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownloadTranslationPTO
{
    @QueryParam("fileUri")
    @FormParam("fileUri")
    @NonNull
    private String fileUri;

    @QueryParam("retrievalType")
    @FormParam("retrievalType")
    private RetrievalType retrievalType;

    @QueryParam("includeOriginalStrings")
    @FormParam("includeOriginalStrings")
    private Boolean includeOriginalStrings;
}
