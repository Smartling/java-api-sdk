package com.smartling.api.filetranslations.v2.pto.mt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MtRequest
{
    private String sourceLocaleId;
    private List<String> targetLocaleIds;
    private boolean pseudo;
    private String profileUid;
}
