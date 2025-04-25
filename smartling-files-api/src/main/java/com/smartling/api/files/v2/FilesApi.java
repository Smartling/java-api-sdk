package com.smartling.api.files.v2;

import com.smartling.api.files.v2.pto.DeleteFilePTO;
import com.smartling.api.files.v2.pto.DownloadAllFileTranslationsPTO;
import com.smartling.api.files.v2.pto.DownloadMultipleTranslationsPTO;
import com.smartling.api.files.v2.pto.DownloadTranslationPTO;
import com.smartling.api.files.v2.pto.ExportTranslationsPTO;
import com.smartling.api.files.v2.pto.FileItemPTO;
import com.smartling.api.files.v2.pto.FileLocaleLastModifiedPTO;
import com.smartling.api.files.v2.pto.FileLocaleStatusResponse;
import com.smartling.api.files.v2.pto.FileStatusResponse;
import com.smartling.api.files.v2.pto.FileTypesListPTO;
import com.smartling.api.files.v2.pto.GetFileLastModifiedPTO;
import com.smartling.api.files.v2.pto.GetFilesListPTO;
import com.smartling.api.files.v2.pto.GetRecentlyPublishedFilesPTO;
import com.smartling.api.files.v2.pto.ImportTranslationsPTO;
import com.smartling.api.files.v2.pto.ImportTranslationsResponse;
import com.smartling.api.files.v2.pto.RecentlyPublishedFileItemPTO;
import com.smartling.api.files.v2.pto.RenameFilePto;
import com.smartling.api.files.v2.pto.UploadFilePTO;
import com.smartling.api.files.v2.pto.UploadFileResponse;
import com.smartling.api.v2.client.exception.server.DetailedErrorMessage;
import com.smartling.api.v2.response.EmptyData;
import com.smartling.api.v2.response.ListResponse;
import com.smartling.api.files.v2.resteasy.ext.TranslatedFileMultipart;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import java.io.InputStream;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static javax.ws.rs.core.MediaType.WILDCARD;
import static org.jboss.resteasy.plugins.providers.multipart.MultipartConstants.MULTIPART_MIXED;

@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Path("/files-api/v2")
@DetailedErrorMessage(args = "fileUri")
public interface FilesApi extends AutoCloseable
{
    @POST
    @Path("/projects/{projectId}/file")
    @Consumes(MULTIPART_FORM_DATA)
    UploadFileResponse uploadFile(@PathParam("projectId") String projectId, @MultipartForm UploadFilePTO uploadFilePTO);

    @GET
    @Path("/projects/{projectId}/file")
    @Produces(WILDCARD)
    InputStream downloadSourceFile(@PathParam("projectId") String projectId, @QueryParam("fileUri") String fileUri);

    @GET
    @Path("/projects/{projectId}/file/status")
    FileStatusResponse getFileStatus(@PathParam("projectId") String projectId, @QueryParam("fileUri") String fileUri);

    @GET
    @Path("/projects/{projectId}/locales/{localeId}/file/status")
    FileLocaleStatusResponse getFileLocaleStatus(@PathParam("projectId") String projectId, @PathParam("localeId") String localeId, @QueryParam("fileUri") String fileUri);

    @GET
    @Path("/projects/{projectId}/locales/{localeId}/file")
    @Produces(WILDCARD)
    InputStream downloadTranslatedFile(@PathParam("projectId") String projectId, @PathParam("localeId") String localeId, @BeanParam DownloadTranslationPTO downloadTranslationPTO);

    @GET
    @Path("/projects/{projectId}/locales/{localeId}/file")
    @Produces(MULTIPART_MIXED)
    TranslatedFileMultipart downloadTranslatedFileMultipart(@PathParam("projectId") String projectId, @PathParam("localeId") String localeId, @BeanParam DownloadTranslationPTO downloadTranslationPTO);

    @GET
    @Path("/projects/{projectId}/locales/all/file/zip")
    @Produces(WILDCARD)
    InputStream downloadAllFileTranslations(@PathParam("projectId") String projectId, @BeanParam DownloadAllFileTranslationsPTO downloadAllFileTranslationsPTO);

    @GET
    @Path("/projects/{projectId}/files/zip")
    @Produces(WILDCARD)
    InputStream downloadMultipleFileTranslations(@PathParam("projectId") String projectId, @BeanParam DownloadMultipleTranslationsPTO downloadMultipleTranslationsPTO);

    @GET
    @Path("/projects/{projectId}/files/list")
    ListResponse<FileItemPTO> getFilesList(@PathParam("projectId") String projectId, @BeanParam GetFilesListPTO getFilesListPTO);

    @GET
    @Path("/projects/{projectId}/files/list/recently-published")
    ListResponse<RecentlyPublishedFileItemPTO> getRecentlyPublishedFiles(@PathParam("projectId") String projectId, @BeanParam GetRecentlyPublishedFilesPTO getRecentlyPublishedFilesPTO);

    @GET
    @Path("/files-api/v2/projects/{projectId}/file-types")
    FileTypesListPTO getFilesTypesList(@PathParam("projectId") String projectId);

    @POST
    @Path("/projects/{projectId}/file/rename")
    EmptyData renameFile(@PathParam("projectId") String projectId, RenameFilePto renameFilePto);

    @POST
    @Path("/projects/{projectId}/file/delete")
    EmptyData deleteFile(@PathParam("projectId") String projectId, DeleteFilePTO deleteFilePTO);

    @GET
    @Path("/projects/{projectId}/file/last-modified")
    ListResponse<FileLocaleLastModifiedPTO> getFileLastModified(@PathParam("projectId") String projectId, @BeanParam GetFileLastModifiedPTO getFileLastModifiedPto);

    @GET
    @Path("/projects/{projectId}/locales/{localeId}/file/last-modified")
    FileLocaleLastModifiedPTO getFileLocaleLastModified(@PathParam("projectId") String projectId, @PathParam("localeId") String localeId, @BeanParam GetFileLastModifiedPTO getFileLastModifiedPto);

    @POST
    @Path("/projects/{projectId}/locales/{localeId}/file/import")
    @Consumes(MULTIPART_FORM_DATA)
    ImportTranslationsResponse importTranslations(@PathParam("projectId") String projectId, @PathParam("localeId") String localeId, @MultipartForm ImportTranslationsPTO importTranslationsPTO);

    @POST
    @Path("/projects/{projectId}/locales/{localeId}/file/get-translations")
    @Consumes(MULTIPART_FORM_DATA)
    @Produces(WILDCARD)
    InputStream exportTranslations(@PathParam("projectId") String projectId, @PathParam("localeId") String localeId, @MultipartForm ExportTranslationsPTO exportTranslationsPTO);
}
