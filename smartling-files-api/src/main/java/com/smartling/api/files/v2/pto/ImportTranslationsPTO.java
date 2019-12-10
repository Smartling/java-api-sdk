package com.smartling.api.files.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.jboss.resteasy.annotations.providers.multipart.PartFilename;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import java.io.InputStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportTranslationsPTO
{
    @FormParam("file")
    @PartType("application/octet-stream")
    @PartFilename("file")
    @NonNull
    private InputStream file;

    @FormParam("fileUri")
    @PartType("text/plain")
    @NonNull
    private String fileUri;

    @FormParam("fileType")
    @PartType("text/plain")
    @NonNull
    private String fileType;

    @FormParam("translationState")
    @PartType("text/plain")
    @NonNull
    private TranslationState translationState;

    @FormParam("overwrite")
    @PartType("text/plain")
    private Boolean overwrite;
}
