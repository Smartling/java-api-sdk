package com.smartling.api.glossary.v3.components.entry;

import com.smartling.api.glossary.v3.components.Common;
import com.smartling.api.glossary.v3.pto.AsyncOperationResponsePTO;
import com.smartling.api.glossary.v3.pto.entry.GlossaryEntryBaseResponsePTO;
import com.smartling.api.glossary.v3.pto.entry.GlossaryEntryDetailedResponsePTO;
import com.smartling.api.glossary.v3.pto.entry.command.EntriesBulkActionCommandPTO;
import com.smartling.api.glossary.v3.pto.entry.command.EntriesBulkUpdateLabelsCommandPTO;
import com.smartling.api.glossary.v3.pto.entry.command.GlossaryEntryCommandPTO;
import com.smartling.api.glossary.v3.pto.entry.command.filter.GetGlossaryEntriesByFilterCommandPTO;
import com.smartling.api.v2.response.ListResponse;
import com.smartling.api.glossary.v3.pto.entry.GlossaryEntryResponsePTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Glossary entries management api.
 */
@Path("/glossary-api/v3")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface EntryManagementApi extends AutoCloseable {
    /**
     * Read glossary entry.
     *
     * @param accountUid  account uid
     * @param glossaryUid glossary uid
     * @param entryUid    glossary entry uid
     * @return {@link GlossaryEntryBaseResponsePTO}
     */
    @GET
    @Path(value = "/accounts/{accountUid}/glossaries/{glossaryUid}/entries/{entryUid}")
    GlossaryEntryDetailedResponsePTO readGlossaryEntry(@PathParam(Common.ACCOUNT_UID) String accountUid,
                                                       @PathParam(Common.GLOSSARY_UID) String glossaryUid,
                                                       @PathParam(Common.ENTRY_UID) String entryUid);

    /**
     * Create glossary entry.
     *
     * @param accountUid  account uid
     * @param glossaryUid glossary uid
     * @param commandPTO  {@link GlossaryEntryCommandPTO}
     * @return {@link GlossaryEntryBaseResponsePTO}
     */
    @POST
    @Path(value = "/accounts/{accountUid}/glossaries/{glossaryUid}/entries")
    GlossaryEntryDetailedResponsePTO createGlossaryEntry(@PathParam(Common.ACCOUNT_UID) String accountUid,
                                                         @PathParam(Common.GLOSSARY_UID) String glossaryUid,
                                                         GlossaryEntryCommandPTO commandPTO
    );

    /**
     * Update glossary entry.
     *
     * @param accountUid  account uid
     * @param glossaryUid glossary uid
     * @param entryUid    glossary entry uid
     * @param commandPTO  {@link GlossaryEntryCommandPTO}
     * @return {@link GlossaryEntryBaseResponsePTO}
     */
    @PUT
    @Path(value = "/accounts/{accountUid}/glossaries/{glossaryUid}/entries/{entryUid}")
    GlossaryEntryDetailedResponsePTO updateGlossaryEntry(@PathParam(Common.ACCOUNT_UID) String accountUid,
                                                         @PathParam(Common.GLOSSARY_UID) String glossaryUid,
                                                         @PathParam(Common.ENTRY_UID) String entryUid,
                                                         GlossaryEntryCommandPTO commandPTO);

    /**
     * Search glossary entries.
     *
     * @param accountUid  account uid
     * @param glossaryUid glossary uid
     * @param commandPTO  {@link GlossaryEntryCommandPTO}
     * @return {@link ListResponse} of {@link GlossaryEntryBaseResponsePTO}
     */
    @POST
    @Path(value = "/accounts/{accountUid}/glossaries/{glossaryUid}/entries/search")
    ListResponse<GlossaryEntryResponsePTO> searchGlossaryEntries(@PathParam(Common.ACCOUNT_UID) String accountUid,
                                                                 @PathParam(Common.GLOSSARY_UID) String glossaryUid,
                                                                 GetGlossaryEntriesByFilterCommandPTO commandPTO);


    /**
     * Archive glossary entries.
     *
     * @param accountUid  account uid
     * @param glossaryUid glossary uid
     * @param commandPTO  {@link EntriesBulkActionCommandPTO}
     * @return {@link AsyncOperationResponsePTO}
     */
    @POST
    @Path(value = "/accounts/{accountUid}/glossaries/{glossaryUid}/entries/archive")
    AsyncOperationResponsePTO archiveGlossaryEntries(@PathParam(Common.ACCOUNT_UID) String accountUid,
                                                     @PathParam(Common.GLOSSARY_UID) String glossaryUid,
                                                     EntriesBulkActionCommandPTO commandPTO);

    /**
     * Unarchive/restore glossary entries.
     *
     * @param accountUid  account uid
     * @param glossaryUid glossary uid
     * @param commandPTO  {@link EntriesBulkActionCommandPTO}
     * @return {@link AsyncOperationResponsePTO}
     */
    @POST
    @Path(value = "/accounts/{accountUid}/glossaries/{glossaryUid}/entries/unarchive")
    AsyncOperationResponsePTO restoreGlossaryEntries(@PathParam(Common.ACCOUNT_UID) String accountUid,
                                                     @PathParam(Common.GLOSSARY_UID) String glossaryUid,
                                                     EntriesBulkActionCommandPTO commandPTO);

    /**
     * Add labels to glossary entries.
     *
     * @param accountUid  account uid
     * @param glossaryUid glossary uid
     * @param commandPTO  {@link EntriesBulkUpdateLabelsCommandPTO}
     * @return {@link AsyncOperationResponsePTO}
     */
    @POST
    @Path(value = "/accounts/{accountUid}/glossaries/{glossaryUid}/entries/add-labels")
    AsyncOperationResponsePTO addLabelsToGlossaryEntries(@PathParam(Common.ACCOUNT_UID) String accountUid,
                                                         @PathParam(Common.GLOSSARY_UID) String glossaryUid,
                                                         EntriesBulkUpdateLabelsCommandPTO commandPTO);

    /**
     * Remove labels from glossary entries.
     *
     * @param accountUid  account uid
     * @param glossaryUid glossary uid
     * @param commandPTO  {@link EntriesBulkUpdateLabelsCommandPTO}
     * @return {@link AsyncOperationResponsePTO}
     */
    @POST
    @Path(value = "/accounts/{accountUid}/glossaries/{glossaryUid}/entries/remove-labels")
    AsyncOperationResponsePTO removeLabelsFromGlossaryEntries(@PathParam(Common.ACCOUNT_UID) String accountUid,
                                                              @PathParam(Common.GLOSSARY_UID) String glossaryUid,
                                                              EntriesBulkUpdateLabelsCommandPTO commandPTO);

    /**
     * Remove glossary entries.
     *
     * @param accountUid  account uid
     * @param glossaryUid glossary uid
     * @param commandPTO  {@link EntriesBulkActionCommandPTO}
     * @return {@link AsyncOperationResponsePTO}
     */
    @POST
    @Path(value = "/accounts/{accountUid}/glossaries/{glossaryUid}/entries/delete")
    AsyncOperationResponsePTO removeGlossaryEntries(@PathParam(Common.ACCOUNT_UID) String accountUid,
                                                    @PathParam(Common.GLOSSARY_UID) String glossaryUid,
                                                    EntriesBulkActionCommandPTO commandPTO
    );

}
