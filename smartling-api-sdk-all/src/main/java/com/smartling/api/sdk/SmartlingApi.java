package com.smartling.api.sdk;

import com.smartling.api.attachments.v2.AttachmentsApi;
import com.smartling.api.contexts.v2.ContextsApi;
import com.smartling.api.files.v2.FilesApi;
import com.smartling.api.issues.v2.IssuesApi;
import com.smartling.api.jobbatches.v2.JobBatchesApi;
import com.smartling.api.locales.v2.LocalesApi;
import com.smartling.api.jobs.v3.TranslationJobsApi;
import com.smartling.api.projects.v2.ProjectsApi;
import com.smartling.api.reports.v3.ReportsApi;
import com.smartling.glossary.v3.GlossaryApi;

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

    /**
     * Returns an API for working with Smartling
     * <a href="https://api-reference.smartling.com/#tag/Files">content files</a>.
     *
     * @return {@link FilesApi}
     */
    FilesApi filesApi();

    /**
     * Returns an API for working with Smartling
     * <a href="https://api-reference.smartling.com/#tag/Account-and-Projects">accounts and projects</a>.
     *
     * @return {@link ProjectsApi}
     */
    ProjectsApi projectsApi();

    /**
     * Returns an API for working with Smartling
     * <a href="https://api-reference.smartling.com/#tag/Attachments">attachments</a>.
     *
     * @return {@link AttachmentsApi}
     */
    AttachmentsApi attachmentsApi();

    /**
     * Returns an API for working with Smartling
     * <a href="https://api-reference.smartling.com/#tag/Context">contexts</a>.
     *
     * @return {@link ContextsApi}
     */
    ContextsApi contextsApi();


    /**
     * Returns an API for working with Smartling
     * <a href="https://api-reference.smartling.com/#tag/Reports">reports</a>.
     *
     * @return {@link ReportsApi}
     */
    ReportsApi reportsApi();

    /**
     * Returns an API for working with Smartling
     * <a href="https://api-reference.smartling.com/#tag/Glossary-API">glossary</a>.
     *
     * @return {@link ReportsApi}
     */
    GlossaryApi glossaryApi();
}
