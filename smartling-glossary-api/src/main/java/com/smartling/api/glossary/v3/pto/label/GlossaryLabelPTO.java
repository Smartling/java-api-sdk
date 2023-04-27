package com.smartling.api.glossary.v3.pto.label;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

/**
 * Label representation.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GlossaryLabelPTO implements ResponseData {
    /**
     * label uuid.
     */
    private UUID labelUid;
    /**
     * Label text.
     * Max size is 50 characters.
     */
    private String labelText;
}
