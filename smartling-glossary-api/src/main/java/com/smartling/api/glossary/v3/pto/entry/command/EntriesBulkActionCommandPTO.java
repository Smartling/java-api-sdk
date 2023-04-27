package com.smartling.api.glossary.v3.pto.entry.command;

import com.smartling.api.glossary.v3.pto.entry.command.filter.GetGlossaryEntriesByFilterCommandPTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * Bulk action filter command.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EntriesBulkActionCommandPTO {
    /**
     * Glossary entries filter.
     */
    @NonNull
    private GetGlossaryEntriesByFilterCommandPTO filter;
}
