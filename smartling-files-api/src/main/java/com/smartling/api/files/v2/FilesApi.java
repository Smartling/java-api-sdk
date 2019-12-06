package com.smartling.api.files.v2;

import com.smartling.api.files.v2.pto.DownloadAllFileTranslationsPTO;
import com.smartling.api.files.v2.pto.DownloadMultipleTranslationsPTO;
import com.smartling.api.files.v2.pto.DownloadTranslationPTO;
import com.smartling.api.files.v2.pto.FileItemPTO;
import com.smartling.api.files.v2.pto.FileLocaleLastModifiedPTO;
import com.smartling.api.files.v2.pto.FileLocaleStatusResponse;
import com.smartling.api.files.v2.pto.FileStatusResponse;
import com.smartling.api.files.v2.pto.FileTypesListPTO;
import com.smartling.api.files.v2.pto.GetFileLastModifiedPTO;
import com.smartling.api.files.v2.pto.GetFilesListPTO;
import com.smartling.api.files.v2.pto.ImportTranslationsPTO;
import com.smartling.api.files.v2.pto.ImportTranslationsResponse;
import com.smartling.api.files.v2.pto.UploadFilePTO;
import com.smartling.api.files.v2.pto.UploadFileResponse;
import com.smartling.api.v2.response.EmptyData;
import com.smartling.api.v2.response.ListResponse;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static javax.ws.rs.core.MediaType.WILDCARD;

@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Path("/files-api/v2")
public interface FilesApi
{
    @POST
    @Path("/projects/{projectId}/file")
    @Consumes(MULTIPART_FORM_DATA)
    UploadFileResponse addFile(@PathParam("projectId") String projectId, @MultipartForm UploadFilePTO uploadFilePTO);

    @GET
    @Path("/projects/{projectId}/file")
    @Produces(WILDCARD)
    byte[] downloadSourceFile(@PathParam("projectId") String projectId, @QueryParam("fileUri") String fileUri);

    @GET
    @Path("/projects/{projectId}/file/status")
    FileStatusResponse getFileStatus(@PathParam("projectId") String projectId, @QueryParam("fileUri") String fileUri);

    @GET
    @Path("/projects/{projectId}/locales/{localeId}/file/status")
    FileLocaleStatusResponse getFileLocaleStatus(@PathParam("projectId") String projectId, @PathParam("localeId") String localeId, @QueryParam("fileUri") String fileUri);

    @GET
    @Path("/projects/{projectId}/locales/{localeId}/file")
    @Produces(WILDCARD)
    byte[] downloadTranslatedFile(@PathParam("projectId") String projectId, @PathParam("localeId") String localeId, @BeanParam DownloadTranslationPTO downloadTranslationPTO);

    @GET
    @Path("/projects/{projectId}/locales/all/file/zip")
    @Produces(WILDCARD)
    byte[] downloadAllFileTranslations(@PathParam("projectId") String projectId, @BeanParam DownloadAllFileTranslationsPTO downloadAllFileTranslationsPTO);

    @GET
    @Path("/projects/{projectId}/files/zip")
    @Produces(WILDCARD)
    byte[] downloadMultipleFileTranslations(@PathParam("projectId") String projectId, @BeanParam DownloadMultipleTranslationsPTO downloadMultipleTranslationsPTO);

    @GET
    @Path("/projects/{projectId}/files/list")
    ListResponse<FileItemPTO> getFilesList(@PathParam("projectId") String projectId, @BeanParam GetFilesListPTO getFilesListPTO);

    @GET
    @Path("/files-api/v2/projects/{projectId}/file-types")
    FileTypesListPTO getFilesTypesList(@PathParam("projectId") String projectId);

    @POST
    @Path("/projects/{projectId}/file/rename")
    @Consumes(MULTIPART_FORM_DATA)
    EmptyData renameFile(@PathParam("projectId") String projectId, @FormParam("fileUri") String fileUri, @FormParam("newFileUri") String newFileUri);

    @POST
    @Path("/projects/{projectId}/file/delete")
    @Consumes(MULTIPART_FORM_DATA)
    EmptyData deleteFile(@PathParam("projectId") String projectId, @FormParam("fileUri") String fileUri);

    @GET
    @Path("/projects/{projectId}/file/last-modified")
    ListResponse<FileLocaleLastModifiedPTO> getFileLastModified(@PathParam("projectId") String projectId, @BeanParam GetFileLastModifiedPTO getFileLastModifiedPto);

    @GET
    @Path("/projects/{projectId}/locales/{localeId}/file/last-modified")
    FileLocaleLastModifiedPTO getFileLocaleLastModified(@PathParam("projectId") String projectId, @PathParam("localeId") String localeId, @BeanParam GetFileLastModifiedPTO getFileLastModifiedPto);

    @POST
    @Path("/projects/{projectId}/locales/{localeId}/file/import")
    @Consumes(MULTIPART_FORM_DATA)
    ImportTranslationsResponse importTranslation(@PathParam("projectId") String projectId, @MultipartForm ImportTranslationsPTO importTranslationsPTO);

    @POST
    @Path("/projects/{projectId}/locales/{localeId}/file/get-translations")
    @Consumes(MULTIPART_FORM_DATA)
    @Produces(WILDCARD)
    byte[] exportTranslation(@PathParam("projectId") String projectId, @MultipartForm DownloadTranslationPTO downloadTranslationPTO);
}
