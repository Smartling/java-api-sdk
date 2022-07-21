package com.smartling.api.jobbatches.v2.pto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BatchItemStatus
{
    UPLOADING("UPLOADING"),
    UPLOAD_FAILED("UPLOAD_FAILED"),
    ATTACHING("ATTACHING"),
    ATTACH_FAILED("ATTACH_FAILED"),
    COMPLETED("COMPLETED"),
    DRAFT("DRAFT"),
    CANCELED("CANCELED");

    @Getter
    @JsonValue
    private final String name;
}
