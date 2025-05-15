package com.smartling.api.mtrouter.v2;


import com.smartling.api.mtrouter.v2.pto.GenerateAccountTranslationCommandPTO;
import com.smartling.api.mtrouter.v2.pto.TranslationPTO;
import com.smartling.api.v2.response.ListResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/mt-router-api/v2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface MtRouterApi extends AutoCloseable
{
    String GENERATE_ACCOUNT_TRANSLATIONS = "/accounts/{accountUid}/smartling-mt";

    @POST
    @Path(GENERATE_ACCOUNT_TRANSLATIONS)
    ListResponse<TranslationPTO> generateAccountTranslations(@PathParam("accountUid") String accountUid, GenerateAccountTranslationCommandPTO command);

}
