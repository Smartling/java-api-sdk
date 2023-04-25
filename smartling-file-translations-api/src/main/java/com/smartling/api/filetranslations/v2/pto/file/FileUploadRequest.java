package com.smartling.api.filetranslations.v2.pto.file;


import com.smartling.api.filetranslations.v2.pto.FileType;

public class FileUploadRequest
{
    private FileType fileType;

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

    @Override
    public String toString()
    {
        return "FileUploadRequest{" +
            "fileType=" + fileType +
            '}';
    }
}
