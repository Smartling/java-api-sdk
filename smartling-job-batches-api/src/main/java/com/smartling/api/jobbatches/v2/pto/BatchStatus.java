package com.smartling.api.jobbatches.v2.pto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BatchStatus
{
    DRAFT,
    ADDING_FILES,
    EXECUTING,
    COMPLETED
}
