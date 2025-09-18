package com.smartling.api.strings.v2;

import com.smartling.api.strings.v2.pto.AsyncStatusResponsePTO;
import com.smartling.api.strings.v2.pto.CreatedStringsListPTO;
import com.smartling.api.strings.v2.pto.GetSourceStringsCommandPTO;
import com.smartling.api.strings.v2.pto.SourceStringListPTO;
import com.smartling.api.strings.v2.pto.StringsCreationPTO;
import com.smartling.api.strings.v2.pto.TranslationsCommandPTO;
import com.smartling.api.strings.v2.pto.TranslationsPTO;
import com.smartling.api.v2.response.ListResponse;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/strings-api/v2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface StringsApi extends AutoCloseable
{
    @POST
    @Path("/projects/{projectUid}")
    CreatedStringsListPTO createStrings(@PathParam("projectUid") String projectUid, StringsCreationPTO stringsCreationPTO);

    @GET
    @Path("/projects/{projectUid}/processes/{processUid}")
    AsyncStatusResponsePTO checkStatus(@PathParam("projectUid") String projectUid, @PathParam("processUid") String processUid);

    @GET
    @Path("/projects/{projectUid}/source-strings")
    SourceStringListPTO getSourceStrings(@PathParam("projectUid") String projectUid, @BeanParam GetSourceStringsCommandPTO sourceStringsCommand);

    @POST
    @Path("/projects/{projectUid}/source-strings")
    SourceStringListPTO getSourceStringsPost(@PathParam("projectUid") String projectUid, GetSourceStringsCommandPTO sourceStringsCommand);

    @GET
    @Path("/projects/{projectUid}/translations")
    ListResponse<TranslationsPTO> getTranslations(@PathParam("projectUid") String projectUid, @BeanParam TranslationsCommandPTO translationsCommand);

    @POST
    @Path("/projects/{projectUid}/translations")
    ListResponse<TranslationsPTO> getTranslationsPost(@PathParam("projectUid") String projectUid, TranslationsCommandPTO translationsCommand);
}
