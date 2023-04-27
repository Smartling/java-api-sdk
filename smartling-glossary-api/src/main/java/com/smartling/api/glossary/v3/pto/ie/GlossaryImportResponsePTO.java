package com.smartling.api.glossary.v3.pto.ie;

import com.smartling.api.v2.response.ResponseData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Glossary import response.
 */
@NoArgsConstructor
@Data
public class GlossaryImportResponsePTO implements ResponseData {
    /**
     * Basic meta information.
     * Uids and status.
     */
    private GlossaryImportPTO glossaryImport;
    /**
     * Provides changes to entries, that will be affected during further steps.
     */
    private ImportEntryChangesPTO entryChanges;

    /**
     * Provides changes to entries translations, that will be affected during further steps.
     */
    private List<ImportEntryTranslationChangesPTO> translationChanges;
    /**
     * Warning messages.
     */
    private List<ImportWarningPTO> warnings;

}
