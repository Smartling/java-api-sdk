package com.smartling.api.filetranslations.v2.pto.file;

import org.jboss.resteasy.annotations.providers.multipart.PartFilename;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import java.io.InputStream;

public class FileUploadPTO
{
    @FormParam("request")
    @PartType("application/json")
    @PartFilename("request")
    private FileUploadRequest request;

    @FormParam("file")
    @PartType("application/octet-stream")
    @PartFilename("file")
    private InputStream file;

    public FileUploadPTO()
    {
    }

    public FileUploadPTO(FileUploadRequest request, InputStream file)
    {
        this.request = request;
        this.file = file;
    }

    public FileUploadRequest getRequest()
    {
        return request;
    }

    public void setRequest(FileUploadRequest request)
    {
        this.request = request;
    }

    public InputStream getFile()
    {
        return file;
    }

    public void setFile(InputStream file)
    {
        this.file = file;
    }

    @Override
    public String toString()
    {
        return "FileUploadPTO{" +
            "request=" + request +
            ", file=" + file +
            '}';
    }
}
