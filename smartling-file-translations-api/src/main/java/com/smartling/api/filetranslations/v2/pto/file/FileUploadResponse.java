package com.smartling.api.filetranslations.v2.pto.file;


import com.smartling.api.v2.response.ResponseData;

public class FileUploadResponse implements ResponseData
{
    private String fileUid;

    private FileUploadResponse()
    {
    }
    public FileUploadResponse(String fileUid)
    {
        this.fileUid = fileUid;
    }

    public String getFileUid()
    {
        return fileUid;
    }

    public void setFileUid(String fileUid)
    {
        this.fileUid = fileUid;
    }

    @Override
    public String toString()
    {
        return "FileUploadResponse{" +
            "fileUid='" + fileUid + '\'' +
            '}';
    }
}
