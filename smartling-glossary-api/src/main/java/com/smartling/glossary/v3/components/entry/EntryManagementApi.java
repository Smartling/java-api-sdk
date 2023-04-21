package com.smartling.glossary.v3.components.entry;

import com.smartling.api.v2.response.ListResponse;
import com.smartling.glossary.v3.pto.AsyncOperationResponsePTO;
import com.smartling.glossary.v3.pto.entry.GlossaryEntryBaseResponsePTO;
import com.smartling.glossary.v3.pto.entry.GlossaryEntryDetailedResponsePTO;
import com.smartling.glossary.v3.pto.entry.GlossaryEntryResponsePTO;
import com.smartling.glossary.v3.pto.entry.command.EntriesBulkActionCommandPTO;
import com.smartling.glossary.v3.pto.entry.command.EntriesBulkUpdateLabelsCommandPTO;
import com.smartling.glossary.v3.pto.entry.command.GlossaryEntryCommandPTO;
import com.smartling.glossary.v3.pto.entry.command.filter.GetGlossariesEntriesByFilterCommandPTO;
import com.smartling.glossary.v3.pto.entry.command.filter.GetGlossaryEntriesByFilterCommandPTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.smartling.glossary.v3.components.Common.ACCOUNT_UID;
import static com.smartling.glossary.v3.components.Common.ENTRY_UID;
import static com.smartling.glossary.v3.components.Common.GLOSSARY_UID;

/**
 * Glossary entries management api.
 */
@Path("/glossary-api/v3")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface EntryManagementApi {
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
    GlossaryEntryDetailedResponsePTO readGlossaryEntry(@PathParam(ACCOUNT_UID) String accountUid,
                                                       @PathParam(GLOSSARY_UID) String glossaryUid,
                                                       @PathParam(ENTRY_UID) String entryUid);

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
    GlossaryEntryDetailedResponsePTO createGlossaryEntry(@PathParam(ACCOUNT_UID) String accountUid,
                                                         @PathParam(GLOSSARY_UID) String glossaryUid,
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
    GlossaryEntryDetailedResponsePTO updateGlossaryEntry(@PathParam(ACCOUNT_UID) String accountUid,
                                                         @PathParam(GLOSSARY_UID) String glossaryUid,
                                                         @PathParam(ENTRY_UID) String entryUid,
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
    ListResponse<GlossaryEntryResponsePTO> searchGlossaryEntries(@PathParam(ACCOUNT_UID) String accountUid,
                                                                 @PathParam(GLOSSARY_UID) String glossaryUid,
                                                                 GetGlossaryEntriesByFilterCommandPTO commandPTO);

    /**
     * Search glossary entries in scope of multiple glossaries.
     *
     * @param accountUid account uid
     * @param commandPTO {@link GlossaryEntryCommandPTO}
     * @return {@link ListResponse} of {@link GlossaryEntryBaseResponsePTO}
     */
    @POST
    @Path(value = "/accounts/{accountUid}/entries/search")
    ListResponse<GlossaryEntryResponsePTO> searchEntriesInMultipleGlossaries(@PathParam(ACCOUNT_UID) String accountUid,
                                                                             GetGlossariesEntriesByFilterCommandPTO commandPTO);

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
    AsyncOperationResponsePTO archiveGlossaryEntries(@PathParam(ACCOUNT_UID) String accountUid,
                                                     @PathParam(GLOSSARY_UID) String glossaryUid,
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
    AsyncOperationResponsePTO restoreGlossaryEntries(@PathParam(ACCOUNT_UID) String accountUid,
                                                     @PathParam(GLOSSARY_UID) String glossaryUid,
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
    AsyncOperationResponsePTO addLabelsToGlossaryEntries(@PathParam(ACCOUNT_UID) String accountUid,
                                                         @PathParam(GLOSSARY_UID) String glossaryUid,
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
    AsyncOperationResponsePTO removeLabelsFromGlossaryEntries(@PathParam(ACCOUNT_UID) String accountUid,
                                                              @PathParam(GLOSSARY_UID) String glossaryUid,
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
    AsyncOperationResponsePTO removeGlossaryEntries(@PathParam(ACCOUNT_UID) String accountUid,
                                                    @PathParam(GLOSSARY_UID) String glossaryUid,
                                                    EntriesBulkActionCommandPTO commandPTO
    );

}
