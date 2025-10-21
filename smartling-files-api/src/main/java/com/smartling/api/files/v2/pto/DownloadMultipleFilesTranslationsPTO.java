package com.smartling.api.files.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownloadMultipleFilesTranslationsPTO
{
    @Singular("file")
    private List<FileLocalePTO> files;
    private RetrievalType retrievalType;
    private Boolean includeOriginalStrings;
    private FileNameMode fileNameMode;
    private LocaleMode localeMode;
    private String zipFileName;
    private FileFilter fileFilter;
}
