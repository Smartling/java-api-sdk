package com.smartling.api.glossary.v3.components.label;

import com.smartling.api.glossary.v3.components.Common;
import com.smartling.api.v2.response.EmptyData;
import com.smartling.api.v2.response.ListResponse;
import com.smartling.api.glossary.v3.pto.label.GlossaryLabelCommandPTO;
import com.smartling.api.glossary.v3.pto.label.GlossaryLabelPTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Glossary labels management api.
 */
@Path("/glossary-api/v3")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface LabelManagementApi extends AutoCloseable {

    /**
     * Read all labels that exists in scope of the account.
     *
     * @param accountUid account identifier
     * @return {@link GlossaryLabelPTO} wrapped into {@link ListResponse}
     */
    @GET
    @Path(value = "/accounts/{accountUid}/labels")
    ListResponse<GlossaryLabelPTO> readAllGlossaryLabels(@PathParam(Common.ACCOUNT_UID) String accountUid);

    /**
     * Create glossary label.
     *
     * @param accountUid   account identifier
     * @param labelCommand {@link GlossaryLabelCommandPTO}
     * @return {@link GlossaryLabelPTO} created
     */
    @POST
    @Path(value = "/accounts/{accountUid}/labels")
    GlossaryLabelPTO createGlossaryLabel(@PathParam(Common.ACCOUNT_UID) String accountUid,
                                         GlossaryLabelCommandPTO labelCommand);

    /**
     * Update glossary label.
     *
     * @param accountUid   account identifier
     * @param labelUid     label identifier
     * @param labelCommand {@link GlossaryLabelCommandPTO}
     * @return {@link GlossaryLabelPTO} created
     */
    @PUT
    @Path(value = "/accounts/{accountUid}/labels/{labelUid}")
    GlossaryLabelPTO updateGlossaryLabel(@PathParam(Common.ACCOUNT_UID) String accountUid,
                                         @PathParam(Common.LABEL_UID) final String labelUid,
                                         GlossaryLabelCommandPTO labelCommand);

    /**
     * Delete glossary label.
     *
     * @param accountUid account identifier
     * @param labelUid   label identifier
     */
    @DELETE
    @Path(value = "/accounts/{accountUid}/labels/{labelUid}")
    EmptyData deleteGlossaryLabel(@PathParam(Common.ACCOUNT_UID) String accountUid,
                                  @PathParam(Common.LABEL_UID) final String labelUid);
}
