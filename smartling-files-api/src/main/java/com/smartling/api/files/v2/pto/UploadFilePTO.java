package com.smartling.api.files.v2.pto;

import com.smartling.api.v2.client.directives.Directives;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.annotations.providers.multipart.PartFilename;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadFilePTO
{
    @FormParam("file")
    @PartType("application/octet-stream")
    @PartFilename("file")
    private InputStream file;

    @FormParam("fileUri")
    @PartType("text/plain")
    private String fileUri;

    @FormParam("fileType")
    @PartType("text/plain")
    private FileType fileType;

    @FormParam("authorize")
    @PartType("text/plain")
    private Boolean authorize;

    @FormParam("localeIdsToAuthorize[]")
    @PartType("text/plain")
    private List<String> localeIdsToAuthorize;

    @FormParam("callbackUrl")
    @PartType("text/plain")
    private String callbackUrl;

    @Directives(prefix="smartling.")
    @PartType("text/plain")
    private Map<String, String> directives;
}
