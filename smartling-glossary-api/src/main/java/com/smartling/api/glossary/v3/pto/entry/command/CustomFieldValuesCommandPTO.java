package com.smartling.api.glossary.v3.pto.entry.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Custom field value command.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomFieldValuesCommandPTO {
    /**
     * Uid of the custom-field.
     */
    @NonNull
    private String fieldUid;
    /**
     * Field value to be assigned.
     * NOTE : Please note that validation will be performed based on field-type.
     * Field-type is external property in scope of 'glossary service', and may be retrieved using 'custom-fields-service'.
     */
    private String fieldValue;
}
