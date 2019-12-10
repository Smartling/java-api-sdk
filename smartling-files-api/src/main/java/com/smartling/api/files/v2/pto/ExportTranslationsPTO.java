package com.smartling.api.files.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.annotations.providers.multipart.PartFilename;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import java.io.InputStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExportTranslationsPTO
{
    @FormParam("file")
    @PartType("application/octet-stream")
    @PartFilename("file")
    private InputStream file;

    @FormParam("fileUri")
    @PartType("text/plain")
    private String fileUri;
}
