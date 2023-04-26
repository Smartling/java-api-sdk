package com.smartling.api.filetranslations.v2.pto.ld;


import com.smartling.api.filetranslations.v2.pto.LanguagePTO;
import com.smartling.api.filetranslations.v2.pto.Error;
import com.smartling.api.v2.response.ResponseData;

import java.util.List;
import java.util.Map;

public class LanguageDetectionStatusResponse implements ResponseData
{
    private LanguageDetectionState state;
    private Error<Map<String,String>> error;
    private List<LanguagePTO> detectedSourceLanguages;

    public LanguageDetectionStatusResponse()
    {
    }

    public LanguageDetectionStatusResponse(LanguageDetectionState state,
            Error<Map<String, String>> error, List<LanguagePTO> detectedSourceLanguages)
    {
        this.state = state;
        this.error = error;
        this.detectedSourceLanguages = detectedSourceLanguages;
    }

    public LanguageDetectionState getState()
    {
        return state;
    }

    public void setState(LanguageDetectionState state)
    {
        this.state = state;
    }

    public Error<Map<String, String>> getError()
    {
        return error;
    }

    public void setError(Error<Map<String, String>> error)
    {
        this.error = error;
    }

    public List<LanguagePTO> getDetectedSourceLanguages()
    {
        return detectedSourceLanguages;
    }

    public void setDetectedSourceLanguages(List<LanguagePTO> detectedSourceLanguages)
    {
        this.detectedSourceLanguages = detectedSourceLanguages;
    }

    @Override
    public String toString()
    {
        return "LanguageDetectionStatusResponse{" +
            "state=" + state +
            ", error=" + error +
            ", detectedSourceLanguages=" + detectedSourceLanguages +
            '}';
    }
}
