package com.smartling.glossary.v3.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Async response with the operation uid.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsyncOperationResponsePTO implements ResponseData {
    /**
     * Operation UID.
     */
    private String operationUid;
}
