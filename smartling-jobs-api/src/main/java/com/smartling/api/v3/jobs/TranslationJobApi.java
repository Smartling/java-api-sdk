package com.smartling.api.v3.jobs;

import com.smartling.api.v3.jobs.pto.AddLocaleCommandPTO;
import com.smartling.api.v3.jobs.pto.AsyncProcessDTO;
import com.smartling.api.v3.jobs.pto.AsyncResponsePTO;
import com.smartling.api.v3.jobs.pto.CustomFieldCreatePTO;
import com.smartling.api.v3.jobs.pto.CustomFieldPTO;
import com.smartling.api.v3.jobs.pto.CustomFieldUpdatePTO;
import com.smartling.api.v3.jobs.pto.PagingCommandPTO;
import com.smartling.api.v3.jobs.pto.SortCommandPTO;
import com.smartling.api.v2.response.EmptyData;
import com.smartling.api.v2.response.ListResponse;
import com.smartling.api.v3.jobs.pto.ContentProgressReportPTO;
import com.smartling.api.v3.jobs.pto.CustomFieldAssignmentPTO;
import com.smartling.api.v3.jobs.pto.FileUriPTO;
import com.smartling.api.v3.jobs.pto.HashcodesAndLocalesPTO;
import com.smartling.api.v3.jobs.pto.LocaleAndHashcodeListCommandPTO;
import com.smartling.api.v3.jobs.pto.StringModifiedCountResponsePTO;
import com.smartling.api.v3.jobs.pto.TranslationJobAddFileCommandPTO;
import com.smartling.api.v3.jobs.pto.TranslationJobAuthorizeCommandPTO;
import com.smartling.api.v3.jobs.pto.TranslationJobCancelCommandPTO;
import com.smartling.api.v3.jobs.pto.TranslationJobCreateCommandPTO;
import com.smartling.api.v3.jobs.pto.TranslationJobCreateResponsePTO;
import com.smartling.api.v3.jobs.pto.TranslationJobGetResponsePTO;
import com.smartling.api.v3.jobs.pto.TranslationJobListCommandPTO;
import com.smartling.api.v3.jobs.pto.TranslationJobListItemPTO;
import com.smartling.api.v3.jobs.pto.TranslationJobLocaleCompletionDatePTO;
import com.smartling.api.v3.jobs.pto.TranslationJobRemoveFileCommandPTO;
import com.smartling.api.v3.jobs.pto.TranslationJobScheduleEditCommandPTO;
import com.smartling.api.v3.jobs.pto.TranslationJobScheduleResponsePTO;
import com.smartling.api.v3.jobs.pto.TranslationJobSearchCommandPTO;
import com.smartling.api.v3.jobs.pto.TranslationJobUpdateCommandPTO;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/jobs-api/v3")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TranslationJobApi
{
    String API_JOBS_ENDPOINT = "/projects/{projectId}/jobs";
    String API_JOBS_SEARCH_ENDPOINT = API_JOBS_ENDPOINT + "/search";
    String API_SINGLE_JOB_ENDPOINT = API_JOBS_ENDPOINT + "/{translationJobUid}";
    String API_JOB_ADD_STRINGS_ENDPOINT = API_SINGLE_JOB_ENDPOINT + "/strings/add";
    String API_JOB_REMOVE_STRINGS_ENDPOINT = API_SINGLE_JOB_ENDPOINT + "/strings/remove";
    String API_JOB_ADD_LOCALE_ENDPOINT = API_SINGLE_JOB_ENDPOINT + "/locales/{localeId}";
    String API_JOB_REMOVE_LOCALE_ENDPOINT = API_SINGLE_JOB_ENDPOINT + "/locales/{localeId}";
    String API_JOB_PROGRESS_ENDPOINT = API_SINGLE_JOB_ENDPOINT + "/progress";
    String API_JOB_ADD_FILE_ENDPOINT = API_SINGLE_JOB_ENDPOINT + "/file/add";
    String API_JOB_FILE_PROGRESS_ENDPOINT = API_SINGLE_JOB_ENDPOINT + "/file/progress";
    String API_JOB_REMOVE_FILE_ENDPOINT = API_SINGLE_JOB_ENDPOINT + "/file/remove";
    String API_JOB_FILES_LIST_ENDPOINT = API_SINGLE_JOB_ENDPOINT + "/files";
    String API_JOB_CANCEL_ENDPOINT = API_SINGLE_JOB_ENDPOINT + "/cancel";
    String API_JOB_CLOSE_ENDPOINT = API_SINGLE_JOB_ENDPOINT + "/close";
    String API_JOB_DELETE_ENDPOINT = API_JOBS_ENDPOINT + "/{translationJobUid}";
    String API_JOB_ASYNC_PROCESSES_ENDPOINT = API_SINGLE_JOB_ENDPOINT + "/processes/{processUid}";
    String API_JOB_AUTHORIZE_ENDPOINT = API_SINGLE_JOB_ENDPOINT + "/authorize";
    String API_JOB_LOCALES_COMPLETION_DATES_ENDPOINT = API_SINGLE_JOB_ENDPOINT + "/locales-completion-dates";

    /* workflow step due date endpoints */
    String API_JOB_SCHEDULE_ENDPOINT = API_SINGLE_JOB_ENDPOINT + "/schedule";

    /* custom fields endpoint */
    String API_ACCOUNT_CUSTOM_FIELDS_ENDPOINT = "/accounts/{accountUid}/custom-fields";
    String API_ACCOUNT_CUSTOM_FIELDS_SINGLE_FIELD_ENDPOINT = "/accounts/{accountUid}/custom-fields/{fieldUid}";
    String API_PROJECT_CUSTOM_FIELDS_ENDPOINT = "/projects/{projectId}/custom-fields";

    String PROJECT_ID = "projectId";
    String ACCOUNT_UID = "accountUid";
    String FIELD_UID = "fieldUid";
    String PROCESS_UID = "processUid";
    String TRANSLATION_JOB_UID = "translationJobUid";
    String LOCALE_ID = "localeId";

    String TARGET_LOCALE_ID = "targetLocaleId";
    String FILE_URI = "fileUri";

    @GET
    @Path(API_JOBS_ENDPOINT)
    ListResponse<TranslationJobListItemPTO> listTranslationJobs(@PathParam(PROJECT_ID) String projectId,
                                                                @BeanParam TranslationJobListCommandPTO translationJobListCommand,
                                                                @BeanParam PagingCommandPTO pagingCommand,
                                                                @BeanParam SortCommandPTO sortCommand);

    @GET
    @Path(API_SINGLE_JOB_ENDPOINT)
    TranslationJobGetResponsePTO getTranslationJob(@PathParam(PROJECT_ID) String projectId, @PathParam(TRANSLATION_JOB_UID) String translationJobUid);

    @POST
    @Path(API_JOBS_ENDPOINT)
    TranslationJobCreateResponsePTO createTranslationJob(@PathParam(PROJECT_ID) String projectId, TranslationJobCreateCommandPTO translationJobCreateCommand);

    @PUT
    @Path(API_SINGLE_JOB_ENDPOINT)
    TranslationJobGetResponsePTO updateTranslationJob(@PathParam(PROJECT_ID) String projectId, @PathParam(TRANSLATION_JOB_UID) String translationJobUid,
                                                      TranslationJobUpdateCommandPTO translationJobUpdateCommand);

    @POST
    @Path(API_JOB_REMOVE_FILE_ENDPOINT)
    StringModifiedCountResponsePTO removeFileFromTranslationJob(@PathParam(PROJECT_ID) String projectId,
                                                                @PathParam(TRANSLATION_JOB_UID) String translationJobUid,
                                                                TranslationJobRemoveFileCommandPTO translationJobFileCommand);

    @POST
    @Path(API_JOB_AUTHORIZE_ENDPOINT)
    EmptyData authorizeTranslationJob(@PathParam(PROJECT_ID) String projectId, @PathParam(TRANSLATION_JOB_UID) String translationJobUid,
                                      TranslationJobAuthorizeCommandPTO translationJobAuthorizeCommand);

    @POST
    @Path(API_JOB_ADD_FILE_ENDPOINT)
    StringModifiedCountResponsePTO addFileToTranslationJob(@PathParam(PROJECT_ID) String projectId, @PathParam(TRANSLATION_JOB_UID) String translationJobUid,
                                                           TranslationJobAddFileCommandPTO translationJobAddFileCommand);

    @POST
    @Path(API_JOB_ADD_STRINGS_ENDPOINT)
    StringModifiedCountResponsePTO addOrMoveStringsToTranslationJob(@PathParam(PROJECT_ID) String projectId,
                                                                    @PathParam(TRANSLATION_JOB_UID) String translationJobUid,
                                                                    LocaleAndHashcodeListCommandPTO localeAndHashcodeListCommand);

    @POST
    @Path(API_JOB_ADD_LOCALE_ENDPOINT)
    AsyncResponsePTO addLocaleToTranslationJob(@PathParam(PROJECT_ID) String projectId, @PathParam(TRANSLATION_JOB_UID) String translationJobUid,
                                               @PathParam(LOCALE_ID) String localeId,
                                               AddLocaleCommandPTO addLocaleCommandPTO);

    @POST
    @Path(API_JOB_REMOVE_STRINGS_ENDPOINT)
    StringModifiedCountResponsePTO removeStringsFromTranslationJob(@PathParam(PROJECT_ID) String projectId,
                                                                   @PathParam(TRANSLATION_JOB_UID) String translationJobUid,
                                                                   HashcodesAndLocalesPTO hashcodesAndLocalesPTO);

    @DELETE
    @Path(API_JOB_REMOVE_LOCALE_ENDPOINT)
    AsyncResponsePTO removeLocaleFromTranslationJob(@PathParam(PROJECT_ID) String projectId, @PathParam(TRANSLATION_JOB_UID) String translationJobUid,
                                                    @PathParam(LOCALE_ID) String localeId);

    @POST
    @Path(API_JOB_CANCEL_ENDPOINT)
    EmptyData cancelTranslationJob(@PathParam(PROJECT_ID) String projectId, @PathParam(TRANSLATION_JOB_UID) String translationJobUid,
                                   TranslationJobCancelCommandPTO translationJobCancelCommand);

    @GET
    @Path(API_JOB_PROGRESS_ENDPOINT)
    ContentProgressReportPTO getTranslationJobProgress(@PathParam(PROJECT_ID) String projectId, @PathParam(TRANSLATION_JOB_UID) String translationJobUid,
                                                       @QueryParam(TARGET_LOCALE_ID) String targetLocaleId);

    @GET
    @Path(API_JOB_FILE_PROGRESS_ENDPOINT)
    ContentProgressReportPTO getTranslationJobFileProgress(@PathParam(PROJECT_ID) String projectId, @PathParam(TRANSLATION_JOB_UID) String translationJobUid,
                                                           @QueryParam(FILE_URI) String fileUri, @QueryParam(TARGET_LOCALE_ID) String targetLocaleId);

    @POST
    @Path(API_JOBS_SEARCH_ENDPOINT)
    ListResponse<TranslationJobListItemPTO> translationJobsSearch(@PathParam(PROJECT_ID) String projectId, TranslationJobSearchCommandPTO searchCommandPTO);

    @DELETE
    @Path(API_JOB_DELETE_ENDPOINT)
    EmptyData deleteTranslationJob(@PathParam(PROJECT_ID) String projectId, @PathParam(TRANSLATION_JOB_UID) String translationJobUid);

    @GET
    @Path(API_JOB_SCHEDULE_ENDPOINT)
    ListResponse<TranslationJobScheduleResponsePTO> getSchedulesByTranslationJob(@PathParam(PROJECT_ID) String projectId,
                                                                                 @PathParam(TRANSLATION_JOB_UID) String translationJobUid);

    @POST
    @Path(API_JOB_SCHEDULE_ENDPOINT)
    ListResponse<TranslationJobScheduleResponsePTO> modifyScheduleForTranslationJob(@PathParam(PROJECT_ID) String projectId,
                                                                                    @PathParam(TRANSLATION_JOB_UID) String translationJobUid,
                                                                                    TranslationJobScheduleEditCommandPTO scheduleEditCommandPTO);

    @GET
    @Path(API_JOB_FILES_LIST_ENDPOINT)
    ListResponse<FileUriPTO> getTranslationJobFiles(@PathParam(PROJECT_ID) String projectId,
                                                    @PathParam(TRANSLATION_JOB_UID) String translationJobUid,
                                                    @BeanParam PagingCommandPTO pagingCommand);

    @POST
    @Path(API_JOB_CLOSE_ENDPOINT)
    EmptyData closeTranslationJob(@PathParam(PROJECT_ID) String projectId, @PathParam(TRANSLATION_JOB_UID) String translationJobUid);

    @GET
    @Path(API_JOB_LOCALES_COMPLETION_DATES_ENDPOINT)
    ListResponse<TranslationJobLocaleCompletionDatePTO> getTranslationJobLocalesCompletionDates(@PathParam(PROJECT_ID) String projectId,
                                                                                                @PathParam(TRANSLATION_JOB_UID) String translationJobUid);

    @POST
    @Path(API_PROJECT_CUSTOM_FIELDS_ENDPOINT)
    EmptyData assignCustomFieldsToProject(@PathParam(PROJECT_ID) String projectId, @BeanParam List<CustomFieldAssignmentPTO> customFieldAssignments);

    @GET
    @Path(API_ACCOUNT_CUSTOM_FIELDS_ENDPOINT)
    ListResponse<CustomFieldPTO> getAccountCustomFields(@PathParam(ACCOUNT_UID) String accountUid, BeanParam filterPTO);

    @POST
    @Path(API_ACCOUNT_CUSTOM_FIELDS_ENDPOINT)
    CustomFieldPTO createCustomField(@PathParam(ACCOUNT_UID) String accountUid, @BeanParam CustomFieldCreatePTO customFieldCreatePTO);

    @PUT
    @Path(API_ACCOUNT_CUSTOM_FIELDS_SINGLE_FIELD_ENDPOINT)
    CustomFieldPTO updateCustomField(@PathParam(ACCOUNT_UID) String accountUid,
                                     @PathParam(FIELD_UID) String fieldUid,
                                     @BeanParam CustomFieldUpdatePTO customFieldUpdatePTO);

    @GET
    @Path(API_JOB_ASYNC_PROCESSES_ENDPOINT)
    AsyncProcessDTO getProcessDetails(@PathParam(PROJECT_ID) String projectId,
                                      @PathParam(TRANSLATION_JOB_UID) String translationJobUid,
                                      @PathParam(PROCESS_UID) String processUid);
}
