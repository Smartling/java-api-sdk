package com.smartling.api.filetranslations.v2.pto.file;

import com.smartling.api.v2.response.ResponseData;

import java.util.List;

public class FileTypesResponse implements ResponseData
{
    private List<String> fileTypes;

    private FileTypesResponse()
    {
    }

    public FileTypesResponse(List<String> fileTypes)
    {
        this.fileTypes = fileTypes;
    }

    public List<String> getFileTypes()
    {
        return fileTypes;
    }

    public void setFileTypes(List<String> fileTypes)
    {
        this.fileTypes = fileTypes;
    }

    @Override
    public String toString()
    {
        return "FileTypesResponse{" +
            "fileTypes=" + fileTypes +
            '}';
    }
}
