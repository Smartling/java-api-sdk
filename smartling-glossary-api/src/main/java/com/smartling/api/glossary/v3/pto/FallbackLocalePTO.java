package com.smartling.api.glossary.v3.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Fallback locales configuration.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FallbackLocalePTO {
    /**
     * Fallback locale id.
     */
    private String fallbackLocaleId;
    /**
     * Locale ids.
     */
    private List<String> localeIds;
}
