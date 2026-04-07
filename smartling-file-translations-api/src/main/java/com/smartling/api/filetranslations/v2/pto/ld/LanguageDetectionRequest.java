package com.smartling.api.filetranslations.v2.pto.ld;

import com.smartling.api.filetranslations.v2.pto.Callback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LanguageDetectionRequest
{
    private Callback callback;
}
