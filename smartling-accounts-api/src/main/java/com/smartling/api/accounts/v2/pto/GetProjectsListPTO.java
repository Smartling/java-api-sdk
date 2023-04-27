package com.smartling.api.accounts.v2.pto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.ws.rs.QueryParam;

/**
 * Filters and pagination parameters for List projects API.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GetProjectsListPTO extends PaginationPTO {
    /**
     * Substring search of the name of the project. Search is case-insensitive.
     */
    @QueryParam("projectNameFilter")
    private String projectNameFilter;

    /**
     * Indicator whether archived projects should be returned. Defaults to {@code false} if not specified.
     */
    @QueryParam("includeArchived")
    private Boolean includeArchived;

    /**
     * Indicator for the type of the project.
     */
    @QueryParam("projectTypeCode")
    private String projectTypeCode;
}
