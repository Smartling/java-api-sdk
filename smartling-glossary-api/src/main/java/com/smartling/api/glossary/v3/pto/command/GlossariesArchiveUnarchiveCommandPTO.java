package com.smartling.api.glossary.v3.pto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

/**
 * Archive / restore glossary data.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlossariesArchiveUnarchiveCommandPTO {
    /**
     * Uids of the glossaries to perform operation.
     */
    @NonNull
    private List<String> glossaryUids;
}
