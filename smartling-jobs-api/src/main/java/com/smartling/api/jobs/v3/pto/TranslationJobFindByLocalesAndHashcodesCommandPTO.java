package com.smartling.api.jobs.v3.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslationJobFindByLocalesAndHashcodesCommandPTO
{
    private List<String> localeIds;
    private List<String> hashcodes;
}
