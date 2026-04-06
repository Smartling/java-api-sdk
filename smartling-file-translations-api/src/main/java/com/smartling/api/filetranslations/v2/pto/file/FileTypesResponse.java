package com.smartling.api.filetranslations.v2.pto.file;

import com.smartling.api.v2.response.ResponseData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileTypesResponse implements ResponseData
{
    private List<String> fileTypes;
}
