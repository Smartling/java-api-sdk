package com.smartling.api.jobbatches.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.annotations.providers.multipart.PartFilename;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadPTO
{
    @FormParam("fileType")
    @PartType("text/plain")
    private String fileType;

    @FormParam("fileUri")
    @PartType("text/plain")
    private String fileUri;

    @FormParam("authorize")
    @PartType("text/plain")
    private Boolean authorize;

    @FormParam("callbackUrl")
    @PartType("text/plain")
    private String callbackUrl;

    @FormParam("localeIdsToAuthorize[]")
    @PartType("text/plain")
    private List<String> localeIdsToAuthorize;

    @FormParam("charset")
    @PartType("text/plain")
    private String charset;

    @FormParam("moveExistingStrings")
    @PartType("text/plain")
    private Boolean moveExistingStrings;

    @FormParam("file")
    @PartType("application/octet-stream")
    @PartFilename("file")
    private byte[] file;

    private Map<String, String> directives;
}
