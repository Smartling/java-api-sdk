package com.smartling.glossary.v3.pto.ie;

import com.smartling.glossary.v3.pto.ie.command.GlossaryImportCommandPTO;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Describes changes to glossary entries - when import process was initiated.
 */
@NoArgsConstructor
@Data
public class ImportEntryChangesPTO {
    /**
     * Amount of glossary entries to be created.
     */
    private int newEntries;
    /**
     * Amount of glossary entries to be updated.
     */
    private int existingEntryUpdates;
    /**
     * Amount of glossary entries that wont be updated.
     */
    private int notMatchedEntries;
    /**
     * Amount of glossary entries to be archived.
     * Please note - {@link GlossaryImportCommandPTO#isArchiveMode()} should be enabled for that purposes.
     */
    private int entriesToArchive;

}
