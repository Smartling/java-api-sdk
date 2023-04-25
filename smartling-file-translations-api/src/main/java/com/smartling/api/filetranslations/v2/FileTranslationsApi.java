package com.smartling.api.filetranslations.v2;

import com.smartling.api.filetranslations.v2.pto.file.FileUploadPTO;
import com.smartling.api.filetranslations.v2.pto.file.FileUploadResponse;
import com.smartling.api.filetranslations.v2.pto.ld.LanguageDetectionResponse;
import com.smartling.api.filetranslations.v2.pto.ld.LanguageDetectionStatusResponse;
import com.smartling.api.filetranslations.v2.pto.mt.MtRequest;
import com.smartling.api.filetranslations.v2.pto.mt.MtResponse;
import com.smartling.api.filetranslations.v2.pto.mt.MtStatusResponse;
import com.smartling.api.v2.response.EmptyData;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.InputStream;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static javax.ws.rs.core.MediaType.WILDCARD;

@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Path("/file-translations-api/v2")
public interface FileTranslationsApi
{
    /**
     * Upload file to perform further actions on it
     *
     * @param accountUid The uid of account
     * @param fileUploadPTO Multipart form containing file and required request parameters
     * @return FileUploadResponse which contains file processing identifier
     */
    @POST
    @Path("/accounts/{accountUid}/files")
    @Consumes(MULTIPART_FORM_DATA)
    FileUploadResponse uploadFile(@PathParam("accountUid") String accountUid, @MultipartForm FileUploadPTO fileUploadPTO);

    /**
     * Trigger MT action for specific file
     *
     * @param accountUid the uid of account.
     * @param fileUid file identifier for which MT is being requested
     * @return response that contains file identifier, that can be used to perform further actions on file
     */
    @POST
    @Path("/accounts/{accountUid}/files/{fileUid}/mt")
    MtResponse mtFile(@PathParam("accountUid") String accountUid, @PathParam("fileUid") String fileUid, MtRequest mtRequest);

    /**
     * Get progress for earlier triggered MT action
     *
     * @param accountUid the uid of account.
     * @param fileUid file identifier for which MT was requested
     * @param mtUid identifier of MT action
     * @return response that contains overall status and per locale MT status
     */
    @GET
    @Path("/accounts/{accountUid}/files/{fileUid}/mt/{mtUid}/status")
    MtStatusResponse getMtProgress(@PathParam("accountUid") String accountUid, @PathParam("fileUid") String fileUid, @PathParam("mtUid") String mtUid);

    /**
     * Fetch translated file for specific locale for earlier requested MT action
     *
     * @param accountUid the uid of account.
     * @param fileUid file identifier for which MT was requested
     * @param mtUid identifier of MT action
     * @param localeId Locale identifier
     * @return data stream of translated file
     */
    @GET
    @Path("/accounts/{accountUid}/files/{fileUid}/mt/{mtUid}/locales/{localeId}/file")
    @Produces(WILDCARD)
    InputStream downloadTranslatedFile(@PathParam("accountUid") String accountUid, @PathParam("fileUid") String fileUid, @PathParam("mtUid") String mtUid,
        @PathParam("localeId") String localeId);

    /**
     * Fetch all translated files for earlier requested MT action
     *
     * @param accountUid the uid of account.
     * @param fileUid file identifier for which MT was requested
     * @param mtUid identifier of MT action
     * @return data stream of zip archive with all the translated files inside
     */
    @GET
    @Path("/accounts/{accountUid}/files/{fileUid}/mt/{mtUid}/locales/all/file/zip")
    @Produces(WILDCARD)
    InputStream downloadAllTranslatedFilesInZip(@PathParam("accountUid") String accountUid, @PathParam("fileUid") String fileUid, @PathParam("mtUid") String mtUid);

    /**
     * Cancel ongoing MT
     *
     * @param accountUid the uid of account.
     * @param fileUid file identifier for which MT was requested
     * @param mtUid identifier of MT action
     */
    @POST
    @Path("/accounts/{accountUid}/files/{fileUid}/mt/{mtUid}/cancel")
    EmptyData cancelMt(@PathParam("accountUid") String accountUid, @PathParam("fileUid") String fileUid, @PathParam("mtUid") String mtUid);

    /**
     * Trigger detect source language for specific file
     *
     * @param accountUid the uid of account.
     * @param fileUid file identifier for which source language detection is being requested
     * @return response that contains language detection identifier, that can be used for other operations
     */
    @POST
    @Path("/accounts/{accountUid}/files/{fileUid}/language-detection")
    LanguageDetectionResponse detectFileSourceLanguage(@PathParam("accountUid") String accountUid, @PathParam("fileUid") String fileUid);

    /**
     * Get progress for earlier triggered source language detection action
     *
     * @param accountUid the uid of account.
     * @param fileUid file identifier for which language detection was requested
     * @param languageDetectionUid identifier of language detection action
     * @return response that contains overall status and per locale MT status
     */
    @GET
    @Path("/accounts/{accountUid}/files/{fileUid}/language-detection/{languageDetectionUid}/status")
    LanguageDetectionStatusResponse getLanguageDetectionStatus(@PathParam("accountUid") String accountUid, @PathParam("fileUid") String fileUid,
        @PathParam("languageDetectionUid") String languageDetectionUid);
}
