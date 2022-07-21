package com.smartling.api.jobbatches.v2.pto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BatchStatus
{
    DRAFT("DRAFT"),
    ADDING_FILES("ADDING_FILES"),
    EXECUTING("EXECUTING"),
    COMPLETED("COMPLETED");

    @Getter
    @JsonValue
    private final String name;
}
