package com.smartling.api.v3.jobs.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslationJobScheduleEditCommandPTO
{
    private List<TranslationJobScheduleEditItemCommandPTO> schedules;
}
