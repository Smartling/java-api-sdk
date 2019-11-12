package com.smartling.api.v2.locales;

import com.smartling.api.v2.locales.pto.ExtendedLocalePTO;
import com.smartling.api.v2.response.ListResponse;
import com.smartling.api.v2.locales.pto.LanguagePTO;
import com.smartling.api.v2.locales.pto.LocalePTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/locales-api/v2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface LocalesApi
{
    /**
     * <p>Endpoint to get list of all locales used in Smartling and available for all customers.
     * Works as a dictionary, but also capable of filtering by specified locale ids.</p>
     *
     * @param localeIds list of desired locales. Might be null or empty. Than all locales are returned
     * @return list of locales
     */
    @GET
    @Path("/dictionary/locales")
    ListResponse<LocalePTO> getLocalesAsDictionary(@QueryParam("localeIds") List<String> localeIds);

    /**
     * <p>Endpoint to get list of all locales used in Smartling and available for all customers.
     * Works as a dictionary, but also capable of filtering by specified locale ids.</p>
     * @param localeIds list of desired locales. Might be null or empty. Than all locales are returned
     * @param supported supported locales only. Might be null or false
     * @return list of locales
     */
    @GET
    @Path("/dictionary/locales")
    ListResponse<LocalePTO> getLocalesAsDictionary(@QueryParam("localeIds") List<String> localeIds, @QueryParam("supportedOnly") Boolean supportedOnly);

    /**
     * <p>Endpoint to get list of all languages used in Smartling and available for all customers.
     * Works as a dictionary, but also capable of filtering by specified language ids.</p>
     *
     * @param languageIds list of desired languages. Might be null or empty. Than all languages are returned
     * @return list of languages
     */
    @GET
    @Path("/dictionary/languages")
    ListResponse<LanguagePTO> getLanguagesAsDictionary(@QueryParam("languageIds") List<String> languageIds);

    /**
     * <p>Endpoint to get list of all locales used in Smartling and available for all customers.
     * Works as a dictionary, but also capable of filtering by specified locale ids.</p>
     *
     * @param localeIds list of desired locales, extended with pluraltag information. Might be null or empty. Than all locales are returned
     * @return list of locales
     */
    @GET
    @Path("/dictionary/extended/locales")
    ListResponse<ExtendedLocalePTO> getLocalesAsDictionaryExtended(@QueryParam("localeIds") List<String> localeIds);
}
