package com.smartling.resteasy.ext.data;

import com.smartling.resteasy.ext.FileFormParam;
import lombok.Data;
import org.jboss.resteasy.annotations.providers.multipart.PartFilename;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import java.io.InputStream;

@Data
public class FormWithFileFormParams
{
    @FileFormParam(value = "file1", filenameField = "file1Name")
    @PartType("application/octet-stream")
    private InputStream file1;

    @FileFormParam("file2")
    @PartFilename("file2")
    @PartType("application/octet-stream")
    private InputStream file2;

    @FileFormParam("file3")
    @PartType("application/octet-stream")
    private InputStream file3;

    @FormParam("file1Name")
    @PartType("text/plain")
    private String file1Name;
}
