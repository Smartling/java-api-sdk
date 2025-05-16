package com.smartling.api.mtrouter.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenerateAccountTranslationCommandPTO
{
    private String sourceLocaleId;
    private String targetLocaleId;
    private List<SourceStringCommandPTO> items;
    private String profileUid;
}
