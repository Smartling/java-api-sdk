package com.smartling.api.strings.v2.pto;

import javax.ws.rs.QueryParam;
import java.util.List;

public class GetSourceStringsCommandPTO extends ListCommandPTO
{
    @QueryParam("fileUri")
    private String fileUri;

    @QueryParam("hashcodes")
    private List<String> hashcodes;

    @QueryParam("showPlaceholderValues")
    private boolean showPlaceholderValues = true;

    public String getFileUri()
    {
        return fileUri;
    }

    public GetSourceStringsCommandPTO setFileUri(String fileUri)
    {
        this.fileUri = fileUri;
        return this;
    }

    public List<String> getHashcodes()
    {
        return hashcodes;
    }

    public GetSourceStringsCommandPTO setHashcodes(List<String> hashcodes)
    {
        this.hashcodes = hashcodes;
        return this;
    }

    public boolean isShowPlaceholderValues()
    {
        return showPlaceholderValues;
    }

    public GetSourceStringsCommandPTO setShowPlaceholderValues(boolean showPlaceholderValues)
    {
        this.showPlaceholderValues = showPlaceholderValues;
        return this;
    }
}
