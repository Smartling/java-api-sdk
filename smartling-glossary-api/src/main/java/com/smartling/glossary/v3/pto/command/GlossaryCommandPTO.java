package com.smartling.glossary.v3.pto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Set;

/**
 * Glossary created/update command data.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlossaryCommandPTO {
    /**
     * Name of the glossary.
     * Character limit is equal to 170 characters.
     */
    @NonNull
    private String glossaryName;
    /**
     * Description of the glossary.
     * Character limit is equal to 250 characters.
     */
    private String description;
    /**
     * Verification mode.
     */
    private boolean verificationMode;
    /**
     * Supported locale ids.
     */
    private Set<String> localeIds;
    /**
     * Fallback locales configuration.
     */
    private List<FallbackLocaleCommandPTO> fallbackLocales;
}
