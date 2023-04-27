package com.smartling.api.glossary.v3.components.ie;

import com.smartling.api.glossary.v3.components.Common;
import com.smartling.api.glossary.v3.pto.ie.GlossaryImportPTO;
import com.smartling.api.glossary.v3.pto.ie.GlossaryImportResponsePTO;
import com.smartling.api.glossary.v3.pto.ie.command.GlossaryEntriesExportCommandPTO;
import com.smartling.api.glossary.v3.pto.ie.command.GlossaryImportCommandPTO;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static javax.ws.rs.core.MediaType.WILDCARD;

/**
 * Glossary import - export api.
 */
@Path("/glossary-api/v3")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ImportExportApi {
    /**
     * Export glossary - by filter.
     * Caller of the method is responsible for closing the stream after reading its content.
     *
     * @param accountUid       account identifier
     * @param glossaryUid      glossary identifier
     * @param exportCommandPTO {@link GlossaryEntriesExportCommandPTO}
     * @return {@link OutputStream}
     */
    @POST
    @Path("/accounts/{accountUid}/glossaries/{glossaryUid}/entries/download")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(WILDCARD)
    InputStream exportGlossary(@PathParam(Common.ACCOUNT_UID) String accountUid,
                               @PathParam(Common.GLOSSARY_UID) String glossaryUid,
                               GlossaryEntriesExportCommandPTO exportCommandPTO);

    /**
     * Initialize Import glossary.
     * Upload file, and perform validation. ( NO changes will be done during this method call ).
     * In case of success - glossary import will be created with 'PENDING' status.
     *
     * @param accountUid  account identifier
     * @param glossaryUid glossary identifier in {@link UUID} format
     * @param command     {@link GlossaryImportCommandPTO} mode + file
     * @return {@link GlossaryImportResponsePTO} with counters and import uid information
     */
    @POST
    @Path(value = "/accounts/{accountUid}/glossaries/{glossaryUid}/import")
    @Consumes(MULTIPART_FORM_DATA)
    GlossaryImportResponsePTO initializeGlossaryImport(@PathParam(Common.ACCOUNT_UID) String accountUid,
                                                       @PathParam(Common.GLOSSARY_UID) String glossaryUid,
                                                       @MultipartForm GlossaryImportCommandPTO command);

    /**
     * Allow to check status of the import process.
     * Statuses :
     * - PENDING - file uploaded and validations passed;
     * - IN_PROGRESS - import confirmed by client - and running;
     * - SUCCESS - import accomplished with success;
     * - FAILED - import process failed.
     *
     * @param accountUid  account identifier
     * @param glossaryUid glossary identifier in {@link UUID} format
     * @param importUid   import identifier in {@link UUID} format
     * @return {@link GlossaryImportPTO}
     */
    @GET
    @Path(value = "/accounts/{accountUid}/glossaries/{glossaryUid}/import/{importUid}")
    GlossaryImportPTO importStatus(@PathParam(Common.ACCOUNT_UID) String accountUid,
                                   @PathParam(Common.GLOSSARY_UID) String glossaryUid,
                                   @PathParam(Common.IMPORT_UID) String importUid);

    /**
     * Confirms glossary import.
     * Only imports with STATUS : 'PENDING' may be confirmed.
     * Import process will be running in async mode, #importStatus property may be used for import process monitoring.
     *
     * @param accountUid  account identifier
     * @param glossaryUid glossary identifier in {@link UUID} format
     * @param importUid   import identifier in {@link UUID} format
     * @return {@link GlossaryImportResponsePTO} with counters and import uid information
     */
    @POST
    @Path(value = "/accounts/{accountUid}/glossaries/{glossaryUid}/import/{importUid}/confirm")
    GlossaryImportPTO confirmGlossaryImport(@PathParam(Common.ACCOUNT_UID) String accountUid,
                                            @PathParam(Common.GLOSSARY_UID) String glossaryUid,
                                            @PathParam(Common.IMPORT_UID) String importUid);
}
