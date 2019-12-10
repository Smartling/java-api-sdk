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
    @NonNull
    private String fileUri;

    @QueryParam("retrievalType")
    private RetrievalType retrievalType;

    @QueryParam("includeOriginalStrings")
    private Boolean includeOriginalStrings;
}
