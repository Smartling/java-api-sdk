package com.smartling.api.files.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslationImportErrorItemPTO
{
    private String fileUri;
    private String importKey;
    private String stringHashcode;
    private List<String> messages;

}
