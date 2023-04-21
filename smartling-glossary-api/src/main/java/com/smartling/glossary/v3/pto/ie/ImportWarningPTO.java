package com.smartling.glossary.v3.pto.ie;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Import related warnings.
 */
@NoArgsConstructor
@Data
public class ImportWarningPTO {
    /**
     * Warning key.
     */
    private String key;
    /**
     * Warning message.
     */
    private String message;
}
