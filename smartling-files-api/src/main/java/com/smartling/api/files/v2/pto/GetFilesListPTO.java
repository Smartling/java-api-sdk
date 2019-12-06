package com.smartling.api.files.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.ws.rs.QueryParam;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GetFilesListPTO extends PagedFilter
{
    @QueryParam("uriMask")
    private String uriMask;

    @QueryParam("fileTypes")
    private List<FileType> fileTypes;

    @QueryParam("lastUploadedAfter")
    private String lastUploadedAfter;

    @QueryParam("lastUploadedBefore")
    private String lastUploadedBefore;

    @QueryParam("orderBy")
    private OrderBy orderBy;
}
