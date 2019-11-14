package com.smartling.api.jobs.v3.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranslationJobSearchCommandPTO
{
    private List<String> hashcodes;
    private List<String> fileUris;
    private List<String> translationJobUids;
}
