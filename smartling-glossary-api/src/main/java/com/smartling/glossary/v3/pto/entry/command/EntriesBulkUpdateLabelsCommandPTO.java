package com.smartling.glossary.v3.pto.entry.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * Bulk glossary entry labels action.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EntriesBulkUpdateLabelsCommandPTO extends EntriesBulkActionCommandPTO {
    /**
     * Identifiers of the glossary-labels.
     */
    @NonNull
    private Set<String> labelUids;
}
