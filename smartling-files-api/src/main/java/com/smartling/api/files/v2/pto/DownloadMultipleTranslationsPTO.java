package com.smartling.api.files.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownloadMultipleTranslationsPTO
{
    @QueryParam("fileUris")
    List<String> fileUris;

    @QueryParam("localeIds")
    private List<String> localeIds;

    @QueryParam("retrievalType")
    private RetrievalType retrievalType;

    @QueryParam("includeOriginalStrings")
    private Boolean includeOriginalStrings;

    @QueryParam("fileNameMode")
    private FileNameMode fileNameMode;

    @QueryParam("localeMode")
    private LocaleMode localeMode;

    @QueryParam("zipFileName")
    private String zipFileName;
}
