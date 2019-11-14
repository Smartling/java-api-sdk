package com.smartling.api.sdk;

import com.smartling.api.jobbatches.v2.JobBatchesApi;
import com.smartling.api.v2.issues.IssuesApi;
import com.smartling.api.locales.v2.LocalesApi;
import com.smartling.api.jobs.v3.TranslationJobsApi;

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
     * Returns an API for working with Smartling
     * <a href="https://api-reference.smartling.com/#tag/Locales">locales</a>.
     *
     * @return {@link LocalesApi}
     */
    LocalesApi localesApi();

    /**
     * Returns an API for working with Smartling
     * <a href="https://api-reference.smartling.com/#tag/Jobs">jobs</a>.
     *
     * @return {@link TranslationJobsApi}
     */
    TranslationJobsApi translationJobsApi();

    /**
     * Returns an API for working with Smartling
     * <a href="https://api-reference.smartling.com/#tag/Job-batches">job batches</a>.
     *
     * @return {@link JobBatchesApi}
     */
    JobBatchesApi jobBatchesApi();
}
