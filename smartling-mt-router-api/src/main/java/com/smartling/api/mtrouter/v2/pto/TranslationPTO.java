package com.smartling.api.mtrouter.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranslationPTO implements ResponseData
{
    private String key;
    private String mtUid;
    private String translationText;
    private ErrorPTO error;
    private String provider;
}
