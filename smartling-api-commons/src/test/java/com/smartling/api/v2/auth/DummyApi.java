package com.smartling.api.v2.auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public interface DummyApi
{
    String DUMMY_API = "/dummy-api/v2/dummy";

    @GET
    @Path(DUMMY_API)
    void dummy();

}
