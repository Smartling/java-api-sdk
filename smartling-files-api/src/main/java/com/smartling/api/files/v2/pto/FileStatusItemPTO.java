package com.smartling.api.files.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileStatusItemPTO
{
    private String localeId;
    private int authorizedStringCount;
    private int authorizedWordCount;
    private int completedStringCount;
    private int completedWordCount;
    private int excludedStringCount;
    private int excludedWordCount;
}
