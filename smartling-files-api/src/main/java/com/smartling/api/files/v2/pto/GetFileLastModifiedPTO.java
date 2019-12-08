package com.smartling.api.files.v2.pto;

import javax.ws.rs.QueryParam;
import java.util.Calendar;

public class GetFileLastModifiedPTO
{
    @QueryParam("fileUri")
    private String fileUri;

    @QueryParam("lastModifiedAfter")
    private Calendar lastModifiedAfter;
}
