package com.smartling.api.filetranslations.v2.pto.ld;


import com.smartling.api.v2.response.ResponseData;

public class LanguageDetectionResponse implements ResponseData
{
    private String languageDetectionUid;

    private LanguageDetectionResponse()
    {
    }
    public LanguageDetectionResponse(String languageDetectionUid)
    {
        this.languageDetectionUid = languageDetectionUid;
    }

    public String getLanguageDetectionUid()
    {
        return languageDetectionUid;
    }

    public void setLanguageDetectionUid(String languageDetectionUid)
    {
        this.languageDetectionUid = languageDetectionUid;
    }

    @Override
    public String toString()
    {
        return "LanguageDetectionResponse{" +
            "languageDetectionUid='" + languageDetectionUid + '\'' +
            '}';
    }
}
