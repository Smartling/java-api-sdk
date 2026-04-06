package com.smartling.api.filetranslations.v2.pto.file;

import com.smartling.api.filetranslations.v2.pto.FileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileUploadRequest
{
    private FileType fileType;
    private List<ParseConfigItem> parseConfigItems;
}
