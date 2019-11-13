package com.smartling.api.locales.v2;

import com.smartling.api.locales.v2.pto.LocalePTO;
import com.smartling.api.v2.response.ListResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/locales-api/v2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface LocalesApi
{
    /**
     * Returns a list of all locales used by Smartling.
     *
     * <p>
     *     The list of returned locales may be filtered by providing a list of
     *     locale identifiers to return.
     * </p>
     *
     * @param localeIds list of desired locales to return, if <code>null</code> or empty, all
     *                  locales will be returned
     * @return list of locales
     */
    @GET
    @Path("/dictionary/locales")
    ListResponse<LocalePTO> getLocalesAsDictionary(@QueryParam("localeIds") List<String> localeIds);

    /**
     * Returns a list of all locales used by Smartling.
     *
     * <p>
     *     The list of returned locales may be filtered by providing a list of
     *     locale identifiers to return and a boolean to indicate if only Smartling supported
     *     locales should be returned.
     * </p>
     *
     * @param localeIds list of desired locales to return, if <code>null</code> or empty, all
     *                  locales will be returned
     * @param supported <code>true</code> if only supported locales should be returned;
     *                  <code>false</code> or <code>null</code> otherwise
     * @return list of locales
     */
    @GET
    @Path("/dictionary/locales")
    ListResponse<LocalePTO> getLocalesAsDictionary(@QueryParam("localeIds") List<String> localeIds, @QueryParam("supportedOnly") Boolean supportedOnly);
}
