package com.smartling.glossary.v3.pto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

/**
 * Configuration of the fallBack locale.
 * Allow to select translations from fallback locale in case if requested locale does not provide translation data.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FallbackLocaleCommandPTO {
    /**
     * Fallback locale id.
     */
    @NonNull
    private String fallbackLocaleId;
    /**
     * Locales ids.
     */
    @NonNull
    private List<String> localeIds;
}
