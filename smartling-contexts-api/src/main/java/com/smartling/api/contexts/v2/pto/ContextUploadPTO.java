package com.smartling.api.contexts.v2.pto;

import javax.ws.rs.FormParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.annotations.providers.multipart.PartFilename;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContextUploadPTO
{
    @FormParam("name")
    @PartType("text/plain")
    private String name;

    @FormParam("dateCreated")
    @PartType("text/plain")
    private String dateCreated;

    @FormParam("content")
    @PartFilename("content")
    @PartType("text/html")
    private byte[] content;
}
