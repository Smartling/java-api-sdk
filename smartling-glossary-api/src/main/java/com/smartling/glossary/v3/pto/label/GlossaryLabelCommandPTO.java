package com.smartling.glossary.v3.pto.label;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

/**
 * Glossary label create/update data.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GlossaryLabelCommandPTO {
    /**
     * Text of the label.
     */
    @NonNull
    private String labelText;
}
