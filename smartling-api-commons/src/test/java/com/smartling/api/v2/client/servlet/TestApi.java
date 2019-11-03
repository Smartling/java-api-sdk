package com.smartling.api.v2.client.servlet;

import com.smartling.api.v2.response.EmptyData;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

@Path("/packages-api/v2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TestApi
{
    @GET
    @Path("/accounts/{accountUid}/packages")
    @Produces()
    TestListPTO getPackageList(@PathParam("accountUid") String accountUid, @BeanParam TestFilterPTO filter);

    @GET
    @Path("/accounts/{accountUid}/packages/{packageUid}")
    @Produces()
    TestDetailsPTO getPackageByUid(@PathParam("accountUid") String accountUid, @PathParam("packageUid") String packageUid);

    @POST
    @Path("/accounts/{accountUid}/packages")
    @Produces()
    TestDetailsPTO createPackage(@PathParam("accountUid") String accountUid, TestCreatePTO linguisticPackageCreateDTO);

    @PUT
    @Path("/accounts/{accountUid}/packages/{packageUid}")
    @Produces()
    TestDetailsPTO updatePackage(@PathParam("accountUid") String accountUid, @PathParam("packageUid") String packageUid, TestUpdatePTO testUpdatePTO);

    @DELETE
    @Path("/accounts/{accountUid}/packages/{packageUid}")
    @Produces()
    EmptyData deletePackage(@PathParam("accountUid") String accountUid, @PathParam("packageUid") String packageUid);

    @GET
    @Path("/accounts/{accountUid}/packages/{packageUid}/export")
    @Produces("text/csv")
    InputStream exportPackage(@PathParam("accountUid") String accountUid, @PathParam("packageUid") String packageUid);
}
