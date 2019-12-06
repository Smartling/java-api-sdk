package com.smartling.api.files.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.FormParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExportTranslationsPTO
{
    @FormParam("fileUri")
    private String fileUri;

    @FormParam("retrievalType")
    private RetrievalType retrievalType;

    @FormParam("includeOriginalStrings")
    private Boolean includeOriginalStrings;
}
