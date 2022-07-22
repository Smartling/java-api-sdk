package com.smartling.api.jobbatches.v2.pto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BatchItemStatus
{
    UPLOADING,
    UPLOAD_FAILED,
    ATTACHING,
    ATTACH_FAILED,
    COMPLETED,
    DRAFT,
    CANCELED
}
