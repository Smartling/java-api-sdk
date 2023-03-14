package com.smartling.api.attachments.v2.pto;

import com.smartling.resteasy.ext.ListFormParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.annotations.providers.multipart.PartFilename;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import java.io.InputStream;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentUploadPTO
{
    @FormParam("file")
    @PartFilename("file")
    @PartType("application/octet-stream")
    private InputStream file;

    @FormParam("name")
    @PartType("text/plain")
    private String name;

    @ListFormParam("entityUids")
    @PartType("text/plain")
    private List<String> entityUids;

    @FormParam("description")
    @PartType("text/plain")
    private String description;
}
