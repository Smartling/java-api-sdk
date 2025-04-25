package com.smartling.api.files.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.ws.rs.QueryParam;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GetRecentlyPublishedFilesPTO extends PagedFilter
{
    @QueryParam("publishedAfter")
    private String publishedAfter;

    @QueryParam("fileUris[]")
    private List<String> fileUris;

    @QueryParam("localeIds[]")
    private List<String> localeIds;
}
