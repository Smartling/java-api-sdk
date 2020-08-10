package com.smartling.api.strings.v2.pto;

import javax.ws.rs.QueryParam;
import java.util.List;

public class TranslationsCommandPTO extends ListCommandPTO
{
    @QueryParam("targetLocaleId")
    private String targetLocaleId;

    @QueryParam("fileUri")
    private String fileUri;

    @QueryParam("hashcodes")
    private List<String> hashcodes;

    @QueryParam("published")
    private boolean published;

    @QueryParam("retrievalType")
    private String retrievalType;

    @QueryParam("showPlaceholderValues")
    private boolean showPlaceholderValues = true;

    public String getTargetLocaleId()
    {
        return targetLocaleId;
    }

    public TranslationsCommandPTO setTargetLocaleId(String targetLocaleId)
    {
        this.targetLocaleId = targetLocaleId;
        return this;
    }

    public String getFileUri()
    {
        return fileUri;
    }

    public TranslationsCommandPTO setFileUri(String fileUri)
    {
        this.fileUri = fileUri;
        return this;
    }

    public List<String> getHashcodes()
    {
        return hashcodes;
    }

    public TranslationsCommandPTO setHashcodes(List<String> hashcodes)
    {
        this.hashcodes = hashcodes;
        return this;
    }

    public boolean isPublished()
    {
        return published;
    }

    public TranslationsCommandPTO setPublished(boolean published)
    {
        this.published = published;
        return this;
    }

    public String getRetrievalType()
    {
        return retrievalType;
    }

    public TranslationsCommandPTO setRetrievalType(String retrievalType)
    {
        this.retrievalType = retrievalType;
        return this;
    }

    public boolean isShowPlaceholderValues()
    {
        return showPlaceholderValues;
    }

    public TranslationsCommandPTO setShowPlaceholderValues(boolean showPlaceholderValues)
    {
        this.showPlaceholderValues = showPlaceholderValues;
        return this;
    }
}
