package com.smartling.api.files.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportTranslationsResponse
{
    private int stringCount;
    private int wordCount;
    private List<TranslationImportErrorItemPTO> translationImportErrors;
}
