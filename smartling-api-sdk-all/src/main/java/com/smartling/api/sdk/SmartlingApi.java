package com.smartling.api.sdk;

import com.smartling.api.v2.issues.IssuesApi;
import com.smartling.api.v2.locales.LocalesApi;

/**
 * Provides APIs for working with the Smartling Platform.
 */
public interface SmartlingApi
{
    /**
     * Returns an API to for working with Smartling
     * <a href="https://api-reference.smartling.com/#tag/Issues">issues</a>.
     *
     * @return {@link IssuesApi}
     */
    IssuesApi issuesApi();

    /**
     * Returns an API to for working with Smartling
     * <a href="https://api-reference.smartling.com/#tag/Locales">locales</a>.
     *
     * @return {@link LocalesApi}
     */
    LocalesApi localesApi();
}
