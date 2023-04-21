package com.smartling.glossary.v3.components.entry.auth;

import com.smartling.glossary.v3.pto.AsyncOperationResponsePTO;
import com.smartling.glossary.v3.pto.authorize.EntriesAuthorizationCommandPTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.smartling.glossary.v3.components.Common.ACCOUNT_UID;
import static com.smartling.glossary.v3.components.Common.GLOSSARY_UID;

/**
 * Glossary entries authorize Api.
 */
@Path("/glossary-api/v3")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface EntryAuthorizeForTranslationApi {

    /**
     * Allow to authorize entries for translation.
     * Creates all required strings, authorize them and add into the JOB.
     *
     * @param accountUid  account identifier
     * @param glossaryUid glossary identifier
     * @param authCommand {@link EntriesAuthorizationCommandPTO}
     * @return {@link AsyncOperationResponsePTO}
     */
    @POST
    @Path(value = "/accounts/{accountUid}/glossaries/{glossaryUid}/entries/authorization")
    AsyncOperationResponsePTO authorizeEntriesForTranslation(@PathParam(ACCOUNT_UID) String accountUid,
                                                             @PathParam(GLOSSARY_UID) String glossaryUid,
                                                             EntriesAuthorizationCommandPTO authCommand);

}
