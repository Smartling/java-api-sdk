package com.smartling.glossary.v3.pto.ie.command;

import com.smartling.glossary.v3.pto.entry.command.filter.GetGlossaryEntriesByFilterCommandPTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

/**
 * Export command data and filter.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GlossaryEntriesExportCommandPTO {
    /**
     * Export format.
     * Supported :
     * - CSV - comma separated values format;
     * - XLSX - excel format;
     * - TBX - format.
     */
    @NonNull
    private String format;
    /**
     * Required when {@link this#format} set to 'TBX' value.
     * Supported :
     * - TBXcoreStructV02 - for TBX v2 core;
     * - TBXcoreStructV03 - for TBX v3 core.
     */
    private String tbxVersion;
    /**
     * Glossary entries filter - for the export.
     */
    @NonNull
    private GetGlossaryEntriesByFilterCommandPTO filter;
    /**
     * First translation - locale in exported file.
     */
    private String focusLocaleId;
    /**
     * Export locales / translations collection.
     */
    private List<String> localeIds;
    /**
     * Allow to skip entries export. Useful for CSV/XLSX templates export.
     */
    private boolean skipEntries;
}
