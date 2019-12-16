package com.smartling.api.files.v2.pto;

import com.smartling.resteasy.ext.DynamicFormParam;
import com.smartling.resteasy.ext.ListFormParam;
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

    @ListFormParam("localeIdsToAuthorize[]")
    @PartType("text/plain")
    private List<String> localeIdsToAuthorize;

    @FormParam("callbackUrl")
    @PartType("text/plain")
    private String callbackUrl;

    @DynamicFormParam()
    @PartType("text/plain")
    private Map<String, String> directives;
}
