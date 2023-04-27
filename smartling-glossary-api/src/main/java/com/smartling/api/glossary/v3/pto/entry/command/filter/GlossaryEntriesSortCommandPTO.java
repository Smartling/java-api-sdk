package com.smartling.api.glossary.v3.pto.entry.command.filter;

import com.smartling.api.glossary.v3.pto.SortCommandPTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Sort command, for glossary entries sort.
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class GlossaryEntriesSortCommandPTO extends SortCommandPTO {

    static final String SORT_BY_TERM = "term";

    static final String SORT_BY_CREATED_DATE = "createdDate";

    static final String SORT_BY_LAST_MODIFIED_DATE = "lastModifiedDate";

    /**
     * Locale id which is applicable in case if {@link GlossaryEntriesSortCommandPTO#SORT_BY_TERM} is selected as sort field.
     */
    private String localeId;

}
