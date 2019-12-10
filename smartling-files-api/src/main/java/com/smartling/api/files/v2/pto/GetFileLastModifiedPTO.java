package com.smartling.api.files.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetFileLastModifiedPTO
{
    @QueryParam("fileUri")
    private String fileUri;

    @QueryParam("lastModifiedAfter")
    private String lastModifiedAfter;
}
