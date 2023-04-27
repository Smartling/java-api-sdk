package com.smartling.api.sdk;

import com.smartling.api.accounts.v2.AccountsApi;
import com.smartling.api.accounts.v2.AccountsApiFactory;
import com.smartling.api.attachments.v2.AttachmentsApi;
import com.smartling.api.attachments.v2.AttachmentsApiFactory;
import com.smartling.api.contexts.v2.ContextsApi;
import com.smartling.api.contexts.v2.ContextsApiFactory;
import com.smartling.api.files.v2.FilesApi;
import com.smartling.api.files.v2.FilesApiFactory;
import com.smartling.api.filetranslations.v2.FileTranslationsApi;
import com.smartling.api.filetranslations.v2.FileTranslationsApiFactory;
import com.smartling.api.jobbatches.v2.JobBatchesApi;
import com.smartling.api.jobbatches.v2.JobBatchesApiFactory;
import com.smartling.api.jobs.v3.TranslationJobsApi;
import com.smartling.api.jobs.v3.TranslationJobsApiFactory;
import com.smartling.api.issues.v2.IssuesApi;
import com.smartling.api.issues.v2.IssuesApiFactory;
import com.smartling.api.locales.v2.LocalesApi;
import com.smartling.api.locales.v2.LocalesApiFactory;
import com.smartling.api.projects.v2.ProjectsApi;
import com.smartling.api.projects.v2.ProjectsApiFactory;
import com.smartling.api.reports.v3.ReportsApi;
import com.smartling.api.reports.v3.ReportsApiFactory;
import com.smartling.api.v2.client.ClientFactory;
import com.smartling.api.glossary.v3.GlossaryApi;
import com.smartling.api.glossary.v3.GlossaryApiFactory;

final class SmartlingApiImpl implements SmartlingApi
{
    private final AccountsApi accountsApi;
    private final IssuesApi issuesApi;
    private final LocalesApi localesApi;
    private final TranslationJobsApi translationJobsApi;
    private final JobBatchesApi jobBatchesApi;
    private final FilesApi filesApi;
    private final ProjectsApi projectsApi;
    private final AttachmentsApi attachmentsApi;
    private final ContextsApi contextsApi;
    private final ReportsApi reportsApi;
    private final GlossaryApi glossaryApi;
    private final FileTranslationsApi fileTranslationsApi;

    SmartlingApiImpl(String userIdentifier, String userSecret)
    {
        this(userIdentifier, userSecret, new ClientFactory());
    }

    SmartlingApiImpl(String userIdentifier, String userSecret, ClientFactory clientFactory)
    {
        // FIXME: use a shared bearer filter
        accountsApi = new AccountsApiFactory(clientFactory).buildApi(userIdentifier, userSecret);
        issuesApi = new IssuesApiFactory(clientFactory).buildApi(userIdentifier, userSecret);
        localesApi = new LocalesApiFactory(clientFactory).buildApi(userIdentifier, userSecret);
        translationJobsApi = new TranslationJobsApiFactory(clientFactory).buildApi(userIdentifier, userSecret);
        jobBatchesApi = new JobBatchesApiFactory(clientFactory).buildApi(userIdentifier, userSecret);
        filesApi = new FilesApiFactory(clientFactory).buildApi(userIdentifier, userSecret);
        projectsApi = new ProjectsApiFactory(clientFactory).buildApi(userIdentifier, userSecret);
        attachmentsApi = new AttachmentsApiFactory(clientFactory).buildApi(userIdentifier, userSecret);
        contextsApi = new ContextsApiFactory(clientFactory).buildApi(userIdentifier, userSecret);
        reportsApi = new ReportsApiFactory(clientFactory).buildApi(userIdentifier, userSecret);
        glossaryApi = new GlossaryApiFactory(clientFactory).buildApi(userIdentifier, userSecret);
        fileTranslationsApi = new FileTranslationsApiFactory(clientFactory).buildApi(userIdentifier, userSecret);
    }

    @Override
    public AccountsApi accountsApi()
    {
        return accountsApi;
    }

    @Override
    public IssuesApi issuesApi()
    {
        return issuesApi;
    }

    @Override
    public LocalesApi localesApi()
    {
        return localesApi;
    }

    @Override
    public TranslationJobsApi translationJobsApi()
    {
        return translationJobsApi;
    }

    @Override
    public JobBatchesApi jobBatchesApi()
    {
        return jobBatchesApi;
    }

    @Override
    public FilesApi filesApi()
    {
        return filesApi;
    }

    @Override
    public ProjectsApi projectsApi()
    {
        return projectsApi;
    }

    @Override
    public AttachmentsApi attachmentsApi()
    {
        return attachmentsApi;
    }

    @Override
    public ContextsApi contextsApi()
    {
        return contextsApi;
    }

    @Override
    public ReportsApi reportsApi()
    {
        return reportsApi;
    }

    @Override
    public GlossaryApi glossaryApi() {
        return glossaryApi;
    }

    @Override
    public FileTranslationsApi fileTranslationsApi()
    {
        return fileTranslationsApi;
    }
}
