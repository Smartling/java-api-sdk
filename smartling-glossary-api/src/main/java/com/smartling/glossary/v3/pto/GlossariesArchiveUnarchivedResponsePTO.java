package com.smartling.glossary.v3.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Async operation response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlossariesArchiveUnarchivedResponsePTO implements ResponseData {
    /**
     * Uids of the glossaries on which operation was performed.
     */
    private List<String> glossaryUids;

}
