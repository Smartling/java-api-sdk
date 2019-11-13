package com.smartling.api.v3.jobs.pto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranslationJobSearchCommandPTO
{
    private List<String> hashcodes;
    private List<String> fileUris;
    private List<String> translationJobUids;
}
