package com.smartling.glossary.v3.components;

import com.smartling.api.v2.response.ListResponse;
import com.smartling.glossary.v3.pto.GlossariesArchiveUnarchivedResponsePTO;
import com.smartling.glossary.v3.pto.GlossaryEntriesCountsResponsePTO;
import com.smartling.glossary.v3.pto.GlossaryResponsePTO;
import com.smartling.glossary.v3.pto.GlossarySearchResponsePTO;
import com.smartling.glossary.v3.pto.command.GetGlossariesByFilterCommandPTO;
import com.smartling.glossary.v3.pto.command.GetGlossariesWithEntriesCountByFilterCommandPTO;
import com.smartling.glossary.v3.pto.command.GlossariesArchiveUnarchiveCommandPTO;
import com.smartling.glossary.v3.pto.command.GlossaryCommandPTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.smartling.glossary.v3.components.Common.ACCOUNT_UID;
import static com.smartling.glossary.v3.components.Common.GLOSSARY_UID;

/**
 * Glossary management api.
 */
@Path("/glossary-api/v3")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GlossaryManagementApi {
    /**
     * Read glossary.
     *
     * @param accountUid  account identifier
     * @param glossaryUid glossary identifier
     * @return {@link GlossaryResponsePTO}
     */
    @GET
    @Path(value = "/accounts/{accountUid}/glossaries/{glossaryUid}")
    GlossaryResponsePTO readGlossary(@PathParam(ACCOUNT_UID) String accountUid,
                                     @PathParam(GLOSSARY_UID) String glossaryUid);

    /**
     * Create glossary.
     *
     * @param accountUid account identifier
     * @param commandPTO {@link GlossaryCommandPTO}
     * @return {@link GlossaryResponsePTO}
     */
    @POST
    @Path(value = "/accounts/{accountUid}/glossaries")
    GlossaryResponsePTO createGlossary(@PathParam(ACCOUNT_UID) String accountUid,
                                       GlossaryCommandPTO commandPTO);

    /**
     * Update glossary.
     *
     * @param accountUid  account identifier
     * @param glossaryUid glossary identifier
     * @param commandPTO  {@link GlossaryCommandPTO}
     * @return {@link GlossaryResponsePTO}
     */
    @PUT
    @Path(value = "/accounts/{accountUid}/glossaries/{glossaryUid}")
    GlossaryResponsePTO updateGlossary(@PathParam(ACCOUNT_UID) String accountUid,
                                       @PathParam(GLOSSARY_UID) String glossaryUid,
                                       GlossaryCommandPTO commandPTO);

    /**
     * Bulk archive glossaries.
     *
     * @param accountUid account identifier
     * @param commandPTO {@link GlossariesArchiveUnarchiveCommandPTO}
     * @return {@link GlossaryResponsePTO}
     */
    @POST
    @Path(value = "/accounts/{accountUid}/glossaries/archive")
    GlossariesArchiveUnarchivedResponsePTO archiveGlossaries(@PathParam(ACCOUNT_UID) String accountUid,
                                                             GlossariesArchiveUnarchiveCommandPTO commandPTO);

    /**
     * Bulk unarchive/restore glossaries.
     *
     * @param accountUid account identifier
     * @param commandPTO {@link GlossariesArchiveUnarchiveCommandPTO}
     * @return {@link GlossaryResponsePTO}
     */
    @POST
    @Path(value = "/accounts/{accountUid}/glossaries/unarchive")
    GlossariesArchiveUnarchivedResponsePTO restoreGlossaries(@PathParam(ACCOUNT_UID) String accountUid,
                                                             GlossariesArchiveUnarchiveCommandPTO commandPTO);

    /**
     * Search glossaries by filter.
     *
     * @param accountUid account identifier
     * @param commandPTO {@link GetGlossariesWithEntriesCountByFilterCommandPTO}
     * @return {@link GlossaryResponsePTO}
     */
    @POST
    @Path(value = "/accounts/{accountUid}/glossaries/search")
    ListResponse<GlossarySearchResponsePTO> searchGlossaries(@PathParam(ACCOUNT_UID) String accountUid,
                                                             GetGlossariesWithEntriesCountByFilterCommandPTO commandPTO);

    /**
     * Search glossaries entries count by filter.
     *
     * @param accountUid account identifier
     * @param commandPTO {@link GetGlossariesByFilterCommandPTO}
     * @return {@link GlossaryResponsePTO}
     */
    @POST
    @Path(value = "/accounts/{accountUid}/glossaries/search/count")
    ListResponse<GlossaryEntriesCountsResponsePTO> searchGlossariesWithEntriesCounts(@PathParam(ACCOUNT_UID) String accountUid,
                                                                                     GetGlossariesByFilterCommandPTO commandPTO);
}
