package com.smartling.resteasy.ext.data;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;

public interface DummyMultipartApi
{
    String DUMMY_MULTIPART_API = "/dummy-api";

    @POST
    @Consumes(MULTIPART_FORM_DATA)
    @Path(DUMMY_MULTIPART_API + "/dynamic-fields")
    void postDynamicParams(@MultipartForm FormWithDynamicParams form);

    @POST
    @Consumes(MULTIPART_FORM_DATA)
    @Path(DUMMY_MULTIPART_API + "/list-fields")
    void postListParams(@MultipartForm FormWithListParams form);

    @POST
    @Consumes(MULTIPART_FORM_DATA)
    @Path(DUMMY_MULTIPART_API + "/file-fields")
    void postFileFormParams(@MultipartForm FormWithFileFormParams form);
}
