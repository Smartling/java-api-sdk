/*
 * Copyright 2020 Smartling, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this work except in compliance with the License.
 * You may obtain a copy of the License in the LICENSE file, or at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.smartling.api.files.v2.FilesApi;
import com.smartling.api.files.v2.FilesApiFactory;
import com.smartling.api.files.v2.pto.DeleteFilePTO;
import com.smartling.api.files.v2.pto.DownloadTranslationPTO;
import com.smartling.api.files.v2.pto.FileItemPTO;
import com.smartling.api.files.v2.pto.FileLocaleLastModifiedPTO;
import com.smartling.api.files.v2.pto.FileStatusResponse;
import com.smartling.api.files.v2.pto.FileType;
import com.smartling.api.files.v2.pto.GetFileLastModifiedPTO;
import com.smartling.api.files.v2.pto.GetFilesListPTO;
import com.smartling.api.files.v2.pto.RenameFilePto;
import com.smartling.api.files.v2.pto.RetrievalType;
import com.smartling.api.files.v2.pto.UploadFilePTO;
import com.smartling.api.files.v2.pto.UploadFileResponse;
import com.smartling.api.v2.authentication.AuthenticationApi;
import com.smartling.api.v2.authentication.AuthenticationApiFactory;
import com.smartling.api.v2.client.ClientFactory;
import com.smartling.api.v2.client.DefaultClientConfiguration;
import com.smartling.api.v2.client.HttpClientConfiguration;
import com.smartling.api.v2.client.auth.Authenticator;
import com.smartling.api.v2.client.auth.BearerAuthSecretFilter;
import com.smartling.api.v2.client.useragent.LibNameVersionHolder;
import com.smartling.api.v2.response.ListResponse;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FilesApiExample
{
    private static final String   USER_ID       = "YOUR-USER-ID";
    private static final String   USER_SECRET   = "YOUR-USER-SECRET";
    private static final String   PROJECT_ID    = "YOUR-PROJECT-ID";
    private static final String   LOCALE        = "YOUR-LOCALE";

    private static final String   PATH_TO_FILE  = "path-to-a-file";
    private static final FileType FILE_TYPE     = FileType.JAVA_PROPERTIES;
    private static final String   CALLBACK_URL  = null;

    public static void main(String[] args) throws Exception
    {
        FilesApi filesApi = createFilesApiClient(USER_ID, USER_SECRET);

        File file = new File(PATH_TO_FILE);
        String fileUri = file.getName();

        // upload the file
        UploadFilePTO uploadFilePto = UploadFilePTO.builder()
            .file(new FileInputStream(PATH_TO_FILE))
            .fileUri(fileUri)
            .fileType(FILE_TYPE)
            .localeIdsToAuthorize(Collections.singletonList("LOCALE"))
            .callbackUrl(CALLBACK_URL)
            .build();
        UploadFileResponse uploadFileResponse = filesApi.uploadFile(PROJECT_ID, uploadFilePto);
        System.out.println(uploadFileResponse);

        // get last modified date
        GetFileLastModifiedPTO getFileLastModifiedPTO = GetFileLastModifiedPTO.builder()
            .fileUri(fileUri)
            .build();
        FileLocaleLastModifiedPTO fileLocaleLastModified = filesApi.getFileLocaleLastModified(PROJECT_ID, LOCALE, getFileLastModifiedPTO);
        System.out.println(fileLocaleLastModified);

        // rename the file
        final String newFileUri = "newTestFileUri";
        RenameFilePto renameFilePto = RenameFilePto.builder()
            .fileUri(fileUri)
            .newFileUri(newFileUri)
            .build();
        filesApi.renameFile(PROJECT_ID, renameFilePto);

        // run a search for files
        GetFilesListPTO getFilesListPTO = GetFilesListPTO.builder()
            .uriMask(newFileUri)
            .build();
        ListResponse<FileItemPTO> filesList = filesApi.getFilesList(PROJECT_ID, getFilesListPTO);
        System.out.println(filesList);

        // check the file status
        FileStatusResponse fileStatus = filesApi.getFileStatus(PROJECT_ID, newFileUri);
        System.out.println(fileStatus);

        // get the file back, including any translations that have been published.
        DownloadTranslationPTO downloadTranslationPTO = DownloadTranslationPTO.builder()
            .fileUri(newFileUri)
            .retrievalType(RetrievalType.PUBLISHED)
            .build();
        InputStream translatedFile = filesApi.downloadTranslatedFile(PROJECT_ID, LOCALE, downloadTranslationPTO);
        System.out.println(IOUtils.toString(translatedFile, UTF_8.name()));

        // delete the file
        DeleteFilePTO deleteFilePTO = DeleteFilePTO.builder()
            .fileUri(newFileUri)
            .build();
        filesApi.deleteFile(PROJECT_ID, deleteFilePTO);
    }

    public static FilesApi createFilesApiClient(String userId, String userSecret)
    {
        ClientFactory clientFactory = new ClientFactory();

        DefaultClientConfiguration clientConfiguration = DefaultClientConfiguration.builder()

            // Set user-friendly user-agent for HTTP calls
            .libNameVersionHolder(new LibNameVersionHolder("example-integration", "1.0.0"))

            .httpClientConfiguration(
                new HttpClientConfiguration()

                    // Override timeouts
                    .setConnectionRequestTimeout(60_000)
                    .setConnectionTimeout(10_000)
                    .setSocketTimeout(10_000)

                // If you need to use proxy
//                    .setProxyHost("proxy.host")
//                    .setProxyPort(3128)

                // If proxy has security
//                    .setProxyUser("proxy user")
//                    .setProxyPassword("proxy pass")
            )
            .build();

        // Auth Api might not need such big timeouts, but it needs at least proxy and threads config.
        AuthenticationApi authenticationApi = new AuthenticationApiFactory(clientFactory).buildApi(clientConfiguration);
        Authenticator authenticator = new Authenticator(userId, userSecret, authenticationApi);

        return new FilesApiFactory(clientFactory)
            .buildApi(new BearerAuthSecretFilter(authenticator), clientConfiguration);
    }
}
