package com.smartling.api.filetranslations.v2.pto.file;


public class FileUploadRequest
{
    private String fileType;

    public FileUploadRequest()
    {
    }

    public FileUploadRequest(String fileType)
    {
        this.fileType = fileType;
    }

    public String getFileType()
    {
        return fileType;
    }

    public void setFileType(String fileType)
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
