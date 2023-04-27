package com.smartling.api.glossary.v3.pto.ie.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.jboss.resteasy.annotations.providers.multipart.PartFilename;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * Glossary entries import meta data.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GlossaryImportCommandPTO {
    /**
     * In case if {@code true} passed - all entries that are not in import file will be archived.
     */
    @FormParam("archiveMode")
    @PartType(MediaType.TEXT_PLAIN)
    private boolean archiveMode;
    /**
     * Import file itself.
     */
    @FormParam("importFile")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    @PartFilename("importFile")
    @NonNull
    private InputStream importFile;

    /**
     * Import file name.
     */
    @FormParam("importFileName")
    @PartType(MediaType.TEXT_PLAIN)
    @NonNull
    private String importFileName;

    /**
     * Import file type.
     * One of :
     * - text/csv - for csv file,
     * - text/xml - for any tbx file,
     * - application/vnd.openxmlformats-officedocument.spreadsheetml.sheet - for xlsx file.
     */
    @FormParam("importFileMediaType")
    @PartType(MediaType.TEXT_PLAIN)
    @NonNull
    private String importFileMediaType;

}
