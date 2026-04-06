package com.smartling.api.filetranslations.v2.pto.file;

import com.smartling.api.filetranslations.v2.pto.FileType;

import java.util.List;

public class FileUploadRequest
{
    private FileType fileType;
    private List<ParseConfigItem> parseConfigItems;

    public FileUploadRequest()
    {
    }

    public FileUploadRequest(FileType fileType)
    {
        this.fileType = fileType;
    }

    public FileType getFileType()
    {
        return fileType;
    }

    public void setFileType(FileType fileType)
    {
        this.fileType = fileType;
    }

    public List<ParseConfigItem> getParseConfigItems()
    {
        return parseConfigItems;
    }

    public void setParseConfigItems(List<ParseConfigItem> parseConfigItems)
    {
        this.parseConfigItems = parseConfigItems;
    }

    @Override
    public String toString()
    {
        return "FileUploadRequest{" +
            "fileType=" + fileType +
            ", parseConfigItems=" + parseConfigItems +
            '}';
    }
}
