package com.smartling.api.glossary.v3.pto.ie;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Glossary import meta-data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlossaryImportPTO implements ResponseData {
    /**
     * UUID of the imported glossary.
     */
    private String glossaryUid;
    /**
     * UUID of the created import.
     * Required to be used in further steps.
     */
    private String importUid;
    /**
     * Status of the import process.
     * One of :
     * - PENDING,
     * - IN_PROGRESS,
     * - SUCCESSFUL,
     * - FAILED.
     */
    private String importStatus;
}
